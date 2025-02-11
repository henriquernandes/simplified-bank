package com.henriquernandes.PicPay.services;

import com.henriquernandes.PicPay.dtos.LoginUserDTO;
import com.henriquernandes.PicPay.dtos.UserDTO;
import com.henriquernandes.PicPay.entities.User;
import com.henriquernandes.PicPay.enums.UserType;
import com.henriquernandes.PicPay.repositories.UserRepository;
import com.henriquernandes.PicPay.usecases.UserServiceUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService implements UserServiceUseCase {
    @Autowired
    UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void canMakeTransfer(User user, BigDecimal value) throws Exception {
        if (user.getType() == UserType.SHOPKEEPER) {
            throw new Exception("Shopkeepers can only receive transfers!");
        }

        if (user.getBalance().compareTo(value) < 0) {
            throw new Exception("You do not have this value to send!");
        }
    }

    public User createUser(UserDTO userDTO) throws Exception{
        if(userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new Exception("Email already registered!");
        }

        if (userRepository.findByCpfCnpj(userDTO.cpfCnpj()).isPresent()) {
            throw new Exception("CPF/CNPJ already registered!");
        }

        User newUser = new User(userDTO);
        newUser.setPassword(passwordEncoder.encode(userDTO.password()));

        return userRepository.save(newUser);
    }

    public User authenticate(LoginUserDTO loginUserDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDTO.email(), loginUserDTO.password()));

        return userRepository.findByEmail(loginUserDTO.email()).orElseThrow();
    }

    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (User) authentication.getPrincipal();
    }

    public User findByCpfCnpj(String cpfCnpj) throws Exception {
        return this.userRepository.findByCpfCnpj(cpfCnpj).orElseThrow(() -> new Exception("User not found!"));
    }

    public void updateBalances(User payer, User payee, BigDecimal value) {
        payer.setBalance(payer.getBalance().subtract(value));
        payee.setBalance(payee.getBalance().add(value));

        userRepository.save(payer);
        userRepository.save(payee);
    }
}
