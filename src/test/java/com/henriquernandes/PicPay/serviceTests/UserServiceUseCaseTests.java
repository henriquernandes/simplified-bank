package com.henriquernandes.PicPay.serviceTests;

import com.henriquernandes.PicPay.dtos.LoginUserDTO;
import com.henriquernandes.PicPay.dtos.UserDTO;
import com.henriquernandes.PicPay.entities.User;
import com.henriquernandes.PicPay.enums.UserType;
import com.henriquernandes.PicPay.repositories.UserRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserServiceUseCaseTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    @InjectMocks
    UserService userService;

    private User consumer;
    private User shopkeeper;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.consumer = new User("consumer", "consumerName", "53544699028", "consumer@mail.com", "secret", BigDecimal.valueOf(100), UserType.CONSUMER);
        this.shopkeeper = new User("shopkeeper", "shopkeeperName", "53544699029", "shopkeeper@mail.com", "secret", BigDecimal.valueOf(100), UserType.SHOPKEEPER);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(consumer);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testShopkeeperCannotTransfer() throws Exception{
       assertThrows(Exception.class, () -> userService.canMakeTransfer(shopkeeper, BigDecimal.valueOf(10)), "Shopkeepers can only receive transfers!");
    }

    @Test
    public void testConsumerWithBalanceCanTransfer() throws Exception {
        assertDoesNotThrow(() -> userService.canMakeTransfer(consumer, BigDecimal.valueOf(10)));
    }

    @Test
    public void testConsumerWithoutBalanceCannotTransfer() {
        assertThrows(Exception.class, () -> userService.canMakeTransfer(consumer, BigDecimal.valueOf(200)), "You do not have this value to send!");
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateUserConsumer() throws Exception {
        UserDTO userDTO = new UserDTO("newUser", "newUserName", "53544699030", "new@mail.com", "secret", BigDecimal.ZERO, UserType.CONSUMER);
        User newUser = userService.createUser(userDTO);

        assertNotNull(newUser);
        assertEquals(userDTO.firstName(), newUser.getFirstName());
        assertEquals(userDTO.lastName(), newUser.getLastName());
        assertEquals(userDTO.cpfCnpj(), newUser.getCpfCnpj());
        assertEquals(userDTO.email(), newUser.getEmail());
        assertEquals(userDTO.balance(), newUser.getBalance());
        assertEquals(userDTO.type(), newUser.getType());
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateUserShopkeeper() throws Exception {
        UserDTO userDTO = new UserDTO("newUser", "newUserName", "53544699030", "new@mail.com", "secret", BigDecimal.ZERO, UserType.SHOPKEEPER);

        User newUser = userService.createUser(userDTO);

        assertNotNull(newUser);
        assertEquals(userDTO.firstName(), newUser.getFirstName());
        assertEquals(userDTO.lastName(), newUser.getLastName());
        assertEquals(userDTO.cpfCnpj(), newUser.getCpfCnpj());
        assertEquals(userDTO.email(), newUser.getEmail());
        assertEquals(userDTO.balance(), newUser.getBalance());
        assertEquals(userDTO.type(), newUser.getType());
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateUserWithEmailAlreadyRegistered() throws Exception {
        UserDTO userDTO = new UserDTO("newUser", "newUserName", "53544699030", "new@mail.com", "secret", BigDecimal.ZERO, UserType.SHOPKEEPER);
        userService.createUser(userDTO);

        UserDTO userWithSameMailDTO = new UserDTO("newUser", "newUserName", "53544699050", "new@mail.com", "secret", BigDecimal.ZERO, UserType.SHOPKEEPER);

        assertThrows(Exception.class, () -> userService.createUser(userWithSameMailDTO), "Email already registered!");
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateUserWithCpfCnpjAlreadyRegistered() throws Exception {
        UserDTO userDTO = new UserDTO("newUser", "newUserName", "53544699030", "new@mail.com", "secret", BigDecimal.ZERO, UserType.SHOPKEEPER);
        userService.createUser(userDTO);

        UserDTO userWithSameMailDTO = new UserDTO("newUser", "newUserName", "53544699030", "old@mail.com", "secret", BigDecimal.ZERO, UserType.SHOPKEEPER);

        assertThrows(Exception.class, () -> userService.createUser(userWithSameMailDTO), "CPF/CNPJ already registered!");
    }

    @Test
    @Transactional
    @Rollback
    public void testAuthenticateUser() throws Exception{
        userService.createUser(new UserDTO(consumer.getFirstName(), consumer.getLastName(), consumer.getCpfCnpj(), consumer.getEmail(), consumer.getPassword(), consumer.getBalance(), consumer.getType()));

        LoginUserDTO loginUserDTO = new LoginUserDTO(consumer.getEmail(), consumer.getPassword());
        User authenticatedUser = userService.authenticate(loginUserDTO);

        assertNotNull(authenticatedUser);
        assertEquals(consumer.getEmail(), authenticatedUser.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testAuthenticateUserThatDoesNotExist() throws Exception{
        LoginUserDTO loginUserDTO = new LoginUserDTO(consumer.getEmail(), consumer.getPassword());
        assertThrows(Exception.class,() -> userService.authenticate(loginUserDTO), "User not found!");

    }

    @Test
    @Transactional
    @Rollback
    public void testFindByCpfCnpjSuccess() throws Exception {
        userService.createUser(new UserDTO(consumer.getFirstName(), consumer.getLastName(), consumer.getCpfCnpj(), consumer.getEmail(), consumer.getPassword(), consumer.getBalance(), consumer.getType()));
        User foundUser = userService.findByCpfCnpj(consumer.getCpfCnpj());

        assertNotNull(foundUser);
        assertEquals(consumer.getCpfCnpj(), foundUser.getCpfCnpj());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByCpfCnpjNotFound() {
        Exception exception = assertThrows(Exception.class, () -> userService.findByCpfCnpj("00000000000"));
        assertEquals("User not found!", exception.getMessage());
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateBalancesSuccess() {
        BigDecimal transferAmount = BigDecimal.valueOf(10);
        userService.updateBalances(consumer, shopkeeper, transferAmount);

        User updatedConsumer = userRepository.findById(consumer.getId()).orElse(null);
        User updatedShopkeeper = userRepository.findById(shopkeeper.getId()).orElse(null);

        assertNotNull(updatedConsumer);
        assertNotNull(updatedShopkeeper);
        assertEquals(BigDecimal.valueOf(90), updatedConsumer.getBalance());
        assertEquals(BigDecimal.valueOf(110), updatedShopkeeper.getBalance());
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateBalancesZeroAmount() {
        BigDecimal transferAmount = BigDecimal.ZERO;
        userService.updateBalances(consumer, shopkeeper, transferAmount);

        User updatedConsumer = userRepository.findById(consumer.getId()).orElse(null);
        User updatedShopkeeper = userRepository.findById(shopkeeper.getId()).orElse(null);

        assertNotNull(updatedConsumer);
        assertNotNull(updatedShopkeeper);
        assertEquals(BigDecimal.valueOf(100), updatedConsumer.getBalance());
        assertEquals(BigDecimal.valueOf(100), updatedShopkeeper.getBalance());
    }

}
