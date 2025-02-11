package com.henriquernandes.PicPay.controllers;

import com.henriquernandes.PicPay.dtos.TransactionDTO;
import com.henriquernandes.PicPay.entities.Transaction;
import com.henriquernandes.PicPay.services.TransactionService;
import com.henriquernandes.PicPay.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/transfer")
@RestController
public class TransactionController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> transfer(@RequestBody @Valid TransactionDTO transactionDTO, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        Transaction transaction = transactionService.makeTransfer(transactionDTO);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getHistory() {
        List<Transaction> transactions = transactionService.getHistory();
        return ResponseEntity.ok(transactions);
    }
}
