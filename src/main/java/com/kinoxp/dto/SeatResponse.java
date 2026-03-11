package com.kinoxp.dto;

public record SeatResponse(
        Long seatId,
        Long theaterId,
        int rowNumber,
        int seatNumber
) {}
