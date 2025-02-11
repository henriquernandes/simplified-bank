package com.henriquernandes.PicPay.entities;

import com.henriquernandes.PicPay.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transactions")
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payer_id")
    private User payer;

    @ManyToOne
    @JoinColumn(name = "payee_id")
    private User payee;

    @Column(name = "`value`")
    BigDecimal value;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionType status;

    @Column
    private LocalDateTime createdAt = LocalDateTime.now();

    public Transaction(User payee, User payer, BigDecimal value, TransactionType type) {
        this.payee = payee;
        this.payer = payer;
        this.value = value;
        this.status = type;
    }
}
