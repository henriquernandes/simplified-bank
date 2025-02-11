package com.henriquernandes.PicPay.usecases;

import com.henriquernandes.PicPay.dtos.LoginUserDTO;
import com.henriquernandes.PicPay.dtos.UserDTO;
import com.henriquernandes.PicPay.entities.User;

import java.math.BigDecimal;

public interface UserServiceUseCase {
    void canMakeTransfer(User user, BigDecimal value) throws Exception;
    User createUser(UserDTO userDTO) throws Exception;
    User authenticate(LoginUserDTO loginUserDTO);
    User authenticatedUser();
    User findByCpfCnpj(String cpfCnpj) throws Exception;
    void updateBalances(User payer, User payee, BigDecimal value);
}
