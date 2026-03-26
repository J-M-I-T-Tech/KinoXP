package com.kinoxp.user;

import java.time.LocalDate;

public record LoginResponse(
        Long userId,
        String name,
        LocalDate dateOfBirth,
        Role role
) {}
