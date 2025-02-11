package com.henriquernandes.PicPay.repositories;

import com.henriquernandes.PicPay.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.payer.id = ?1 OR t.payee.id = ?1")
    List<Transaction> findTransactionsMadeOrReceivedByUser(Long userId);
}
