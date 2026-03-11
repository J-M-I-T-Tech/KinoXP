package com.kinoxp.dto;

import com.kinoxp.model.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.List;

public record ReservationRequest(
        @NotNull(message = "Showing id is required")
        Long showingId,

        @NotBlank(message = "Customer name is required")
        String customerName,

        @NotEmpty(message = "At least one seat must be selected")
        List<Long> seatIds,

        @NotNull(message = "Create a user")
        Long userId


) {
}
