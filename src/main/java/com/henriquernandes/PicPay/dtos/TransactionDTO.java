package com.henriquernandes.PicPay.dtos;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionDTO(
        @NotNull String payeeCpfCnpj,
        @NotNull BigDecimal value
) {
}
