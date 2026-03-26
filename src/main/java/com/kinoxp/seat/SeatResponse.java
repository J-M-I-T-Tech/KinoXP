package com.kinoxp.seat;

public record SeatResponse(
        Long seatId,
        Long theaterId,
        int rowNumber,
        int seatNumber
) {}
