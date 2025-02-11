package com.henriquernandes.PicPay.repositoryTests;

import com.henriquernandes.PicPay.dtos.UserDTO;
import com.henriquernandes.PicPay.entities.User;
import com.henriquernandes.PicPay.enums.UserType;
import com.henriquernandes.PicPay.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryTests {
    public static List<UserDTO> users;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public static void setUp() {
        users = Stream.of(
                new UserDTO("Henrique", "Ernandes", "53544699028", "consumer@mail.com", "secret", BigDecimal.ZERO, UserType.CONSUMER),
                new UserDTO("Henrique", "Ernandes", "53544699028", "shopkeeper@mail.com", "secret", BigDecimal.ZERO, UserType.SHOPKEEPER)
        ).toList();
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveUser() {
        String firstName = "Henrique";
        String lastName = "Ernandes";
        String cpfCnpj = "53544699028";
        String email = "consumer@test.com";
        String password = "secret";
        BigDecimal balance = new BigDecimal(0);
        UserType type = UserType.CONSUMER;

        UserDTO userDTO = new UserDTO(firstName, lastName, cpfCnpj, email, password, balance, type);

        User user = new User(userDTO);

        User result = userRepository.save(user);

        assertNotNull(result);
        assertEquals(user, result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getCpfCnpj(), result.getCpfCnpj());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getBalance(), result.getBalance());
        assertEquals(user.getType(), result.getType());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByEmail() {
        User user = new User(users.get(0));
        userRepository.save(user);

        User result = userRepository.findByEmail(user.getEmail()).orElseThrow();

        assertNotNull(result);
        assertEquals(user, result);
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getCpfCnpj(), result.getCpfCnpj());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getBalance(), result.getBalance());
        assertEquals(user.getType(), result.getType());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByCpfCnpj() {
        User user = new User(users.get(0));
        userRepository.save(user);

        User result = userRepository.findByCpfCnpj(user.getCpfCnpj()).orElseThrow();

        assertNotNull(result);
        assertEquals(user, result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getCpfCnpj(), result.getCpfCnpj());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getBalance(), result.getBalance());
        assertEquals(user.getType(), result.getType());
    }


}
