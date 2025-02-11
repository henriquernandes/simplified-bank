package com.henriquernandes.PicPay.usecases;

import com.henriquernandes.PicPay.dtos.TransactionDTO;
import com.henriquernandes.PicPay.entities.Transaction;

import java.util.List;

public interface TransactionServiceUseCase {
    Transaction makeTransfer(TransactionDTO transactionDTO) throws Exception;
    List<Transaction> getHistory();
}