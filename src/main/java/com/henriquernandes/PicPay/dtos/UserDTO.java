package com.henriquernandes.PicPay.dtos;

import com.henriquernandes.PicPay.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;

public record UserDTO(
    @NotNull @NotEmpty String firstName,
    @NotNull @NotEmpty String lastName,
    @NotNull @NotEmpty String cpfCnpj,
    @NotNull @NotEmpty String email,
    @NotNull @NotEmpty String password,
    @NotNull BigDecimal balance,
    @NotNull UserType type
){}
