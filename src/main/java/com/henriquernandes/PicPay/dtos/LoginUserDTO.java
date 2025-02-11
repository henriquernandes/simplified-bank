package com.henriquernandes.PicPay.dtos;

import jakarta.validation.constraints.NotNull;

public record LoginUserDTO(
    @NotNull String email,
    @NotNull String password
) {}
