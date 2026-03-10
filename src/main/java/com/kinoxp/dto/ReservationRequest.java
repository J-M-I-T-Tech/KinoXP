package com.kinoxp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReservationRequest(
        @NotNull(message = "Showing id is required")
        Long showingId,

        @NotBlank(message = "Customer name is required")
        String customerName,

        @NotEmpty(message = "At least one seat must be selected")
        List<Long> seatIds
) {
}
