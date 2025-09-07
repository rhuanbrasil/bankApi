package com.api.bankApi.business.dtos;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record AccountRegisterDto(@NotBlank BigDecimal amount) {
}
