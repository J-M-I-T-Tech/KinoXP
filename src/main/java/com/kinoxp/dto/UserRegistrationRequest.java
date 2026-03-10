package com.kinoxp.dto;

import com.kinoxp.model.user.Role;

import java.time.LocalDate;

public record UserRegistrationRequest(
        String name,
        LocalDate dateOfBirth,
        Role role,
        String password
) {}
