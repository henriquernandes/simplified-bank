package com.henriquernandes.PicPay.controllerTests;

import com.henriquernandes.PicPay.controllers.TransactionController;
import com.henriquernandes.PicPay.dtos.TransactionDTO;
import com.henriquernandes.PicPay.entities.Transaction;
import com.henriquernandes.PicPay.entities.User;
import com.henriquernandes.PicPay.enums.UserType;
import com.henriquernandes.PicPay.services.JwtService;
import com.henriquernandes.PicPay.services.TransactionService;
import com.henriquernandes.PicPay.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@WithMockUser
public class TransactionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TransactionService transactionService;

    @InjectMocks
    TransactionController transactionController;

    private User payer;
    private User payee;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        payer = new User("payer", "payerName", "53544699028", "payer@mail.com", "secret", BigDecimal.valueOf(100), UserType.CONSUMER);
        payee = new User("payee", "payeeName", "53544699029", "payee@mail.com", "secret", BigDecimal.valueOf(100), UserType.SHOPKEEPER);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(payer);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @WithMockUser
    public void testTransferSuccess() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(payee.getCpfCnpj(), BigDecimal.valueOf(10));
        Transaction transaction = new Transaction(payee, payer, BigDecimal.valueOf(10), null);

        when(transactionService.makeTransfer(transactionDTO)).thenReturn(transaction);

        mockMvc.perform(post("/transfer")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"payeeCpfCnpj\":\"53544699029\",\"value\":10}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testTransferValidationError() throws Exception {
        mockMvc.perform(post("/transfer")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"cpfCnpj\":\"\",\"amount\":0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testGetHistory() throws Exception {
        Transaction transaction = new Transaction(payee, payer, BigDecimal.valueOf(10), null);

        when(transactionService.getHistory()).thenReturn(Collections.singletonList(transaction));

        mockMvc.perform(get("/transfer/history")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}