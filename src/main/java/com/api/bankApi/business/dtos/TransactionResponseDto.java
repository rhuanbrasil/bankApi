package com.api.bankApi.business.dtos;

import com.api.bankApi.infra.enums.TransactionEnums;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponseDto(@NotBlank UUID transactionId,
                                     @NotBlank BigDecimal amount,
                                     @NotBlank LocalDateTime timestamp,
                                     @NotBlank TransactionEnums transactionType) {
}
