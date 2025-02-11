package com.henriquernandes.PicPay.serviceTests;

import com.henriquernandes.PicPay.dtos.TransactionDTO;
import com.henriquernandes.PicPay.entities.Transaction;
import com.henriquernandes.PicPay.entities.User;
import com.henriquernandes.PicPay.enums.UserType;
import com.henriquernandes.PicPay.repositories.TransactionRepository;
import com.henriquernandes.PicPay.repositories.UserRepository;
import com.henriquernandes.PicPay.services.NotifyService;
import com.henriquernandes.PicPay.services.TransactionService;
import com.henriquernandes.PicPay.services.TransferAuthorizationService;
import com.henriquernandes.PicPay.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
public class TransactionServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private TransferAuthorizationService transferAuthorizationService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Mock
    private NotifyService notifyService;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    private User payer;
    private User payee;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        payer = new User("payer", "payerName", "53544699028", "payer@mail.com", "secret", BigDecimal.valueOf(100), UserType.CONSUMER);
        payee = new User("payee", "payeeName", "53544699029", "payee@mail.com", "secret", BigDecimal.valueOf(100), UserType.SHOPKEEPER);
        userRepository.save(payer);
        userRepository.save(payee);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(payer);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @Transactional
    public void testMakeTransferSuccess() throws Exception {
        when(transferAuthorizationService.authorizeTransfer()).thenReturn(true);
        when(restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Boolean.class)).thenReturn(new ResponseEntity<>(true, HttpStatus.OK));

        TransactionDTO transactionDTO = new TransactionDTO(payee.getCpfCnpj(), BigDecimal.valueOf(10));
        Transaction result = transactionService.makeTransfer(transactionDTO);

        payer = userRepository.findById(payer.getId()).orElse(null);

        assertNotNull(result);
        assertNotNull(payer);
        assertEquals(BigDecimal.valueOf(90), payer.getBalance());
        assertEquals(BigDecimal.valueOf(110), payee.getBalance());
    }
}