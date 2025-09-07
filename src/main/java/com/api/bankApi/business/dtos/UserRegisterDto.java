package com.api.bankApi.business.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserRegisterDto (@NotBlank(message = "Please, inform your login to complete the register") String login, @NotBlank(message = "Please, inform your password to complete the register") String password) {
}
