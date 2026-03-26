package com.kinoxp.user;

import java.time.LocalDate;

public record UserRegistrationRequest(
        String name,
        LocalDate dateOfBirth,
        Role role,
        String password
) {}
