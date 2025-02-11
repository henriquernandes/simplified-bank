package com.henriquernandes.PicPay.serviceTests;

import com.henriquernandes.PicPay.dtos.NotificationDTO;
import com.henriquernandes.PicPay.dtos.TransactionDTO;
import com.henriquernandes.PicPay.entities.FailedNotification;
import com.henriquernandes.PicPay.entities.Transaction;
import com.henriquernandes.PicPay.entities.User;
import com.henriquernandes.PicPay.enums.UserType;
import com.henriquernandes.PicPay.repositories.FailedNotificationRepository;
import com.henriquernandes.PicPay.services.NotifyService;
import com.henriquernandes.PicPay.services.TransactionService;
import com.henriquernandes.PicPay.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotifyServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserService userService;

    @Mock
    private FailedNotificationRepository failedNotificationRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private NotifyService notifyService;

    private Transaction transaction;
    private User payer;
    private User payee;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        payer = new User("payer", "payerName", "53544699028", "payer@mail.com", "secret", BigDecimal.valueOf(100), UserType.CONSUMER);
        payee = new User("payee", "payeeName", "53544699029", "payee@mail.com", "secret", BigDecimal.valueOf(100), UserType.SHOPKEEPER);
        transaction = new Transaction(payee, payer, BigDecimal.valueOf(10), null);
    }

    @Test
    public void testNotifyPayeeSuccess() {
        notifyService.notifyPayee(transaction);
        verify(restTemplate, times(1)).postForEntity(any(String.class), any(NotificationDTO.class), any(Class.class));
    }

    @Test
    public void testNotifyPayeeFailure() {
        doThrow(new RuntimeException()).when(restTemplate).postForEntity(any(String.class), any(NotificationDTO.class), any(Class.class));
        notifyService.notifyPayee(transaction);
        verify(failedNotificationRepository, times(1)).save(any(FailedNotification.class));
    }
}