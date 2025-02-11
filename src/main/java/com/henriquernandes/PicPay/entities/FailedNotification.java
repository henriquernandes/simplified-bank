package com.henriquernandes.PicPay.entities;

import com.henriquernandes.PicPay.dtos.NotificationDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "failed_notifications")
@Getter
@Setter
@NoArgsConstructor
public class FailedNotification {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column
    private String email;

    @Column
    private String message;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transactionId;

    @Column
    private LocalDateTime createdAt = LocalDateTime.now();

    public FailedNotification(NotificationDTO data, Transaction transaction) {
        this.email = data.email();
        this.message = data.message();
        this.transactionId = transaction;
    }
}
