package com.henriquernandes.PicPay.services;

import com.henriquernandes.PicPay.dtos.TransactionDTO;
import com.henriquernandes.PicPay.entities.Transaction;
import com.henriquernandes.PicPay.entities.User;
import com.henriquernandes.PicPay.enums.TransactionType;
import com.henriquernandes.PicPay.repositories.TransactionRepository;
import com.henriquernandes.PicPay.repositories.UserRepository;
import com.henriquernandes.PicPay.usecases.TransactionServiceUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements TransactionServiceUseCase {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private TransferAuthorizationService transferAuthorizationService;


    public Transaction makeTransfer(TransactionDTO transactionDTO) throws Exception {
        User payer = userService.authenticatedUser();
        User payee = userService.findByCpfCnpj(transactionDTO.payeeCpfCnpj());

        userService.canMakeTransfer(payer, transactionDTO.value());

        Transaction transaction = new Transaction(
                payee,
                payer,
                transactionDTO.value(),
                TransactionType.APPROVED
        );

        if (!transferAuthorizationService.authorizeTransfer()) {
            throw new Exception("Transfer not authorized");
        }

        userService.updateBalances(payer, payee, transactionDTO.value());

        transactionRepository.save(transaction);
        notifyService.notifyPayee(transaction);

        return transaction;
    }

    public List<Transaction> getHistory() {
        User user = userService.authenticatedUser();
        return transactionRepository.findTransactionsMadeOrReceivedByUser(user.getId());
    }

}
