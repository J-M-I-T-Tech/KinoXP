package com.kinoxp.dto;

import com.kinoxp.model.user.Role;

import java.time.LocalDate;

public record LoginResonse(
    Long userId,
    String name,
    LocalDate dateOfBirth,
    Role role
) {
}
