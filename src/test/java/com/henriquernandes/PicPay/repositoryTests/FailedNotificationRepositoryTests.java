package com.henriquernandes.PicPay.repositoryTests;

import com.henriquernandes.PicPay.dtos.NotificationDTO;
import com.henriquernandes.PicPay.dtos.UserDTO;
import com.henriquernandes.PicPay.entities.FailedNotification;
import com.henriquernandes.PicPay.entities.Transaction;
import com.henriquernandes.PicPay.entities.User;
import com.henriquernandes.PicPay.enums.TransactionType;
import com.henriquernandes.PicPay.enums.UserType;
import com.henriquernandes.PicPay.repositories.FailedNotificationRepository;
import com.henriquernandes.PicPay.repositories.TransactionRepository;
import com.henriquernandes.PicPay.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class FailedNotificationRepositoryTests {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FailedNotificationRepository failedNotificationRepository;

    @Test
    @Transactional
    @Rollback
    public void testCanSaveFailedNotification() {

        UserDTO payeeDTO = new UserDTO("Henrique", "Ernandes", "53544699028", "consumer@mail.com", "secret", BigDecimal.valueOf(500), UserType.CONSUMER);
        UserDTO payerDTO = new UserDTO("Henrique", "Ernandes", "53544699030", "shopkeeper@mail.com", "secret", BigDecimal.ZERO, UserType.SHOPKEEPER);

        User payee = userRepository.save(new User(payeeDTO));
        User payer = userRepository.save(new User(payerDTO));

        Transaction transaction = new Transaction(payer, payee, BigDecimal.valueOf(100), TransactionType.APPROVED);
        transactionRepository.save(transaction);

        NotificationDTO notificationDTO = new NotificationDTO(transaction.getPayee().getEmail(),"You have received a payment from " + transaction.getPayer().getFirstName() + " in the value of " + transaction.getValue());

        FailedNotification failedNotification = new FailedNotification(notificationDTO, transaction);
        FailedNotification result = failedNotificationRepository.save(failedNotification);

        assertNotNull(failedNotification);
        assertEquals(result, failedNotification);
    }
}
