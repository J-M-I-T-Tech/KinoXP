package com.kinoxp.dto;

import com.kinoxp.model.reservation.BookingStatus;
import com.kinoxp.model.reservation.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationResponse(
        Long reservationId,
        Long showingId,
        Long movieId,
        String movieTitle,
        String customerName,
        List<Long> seatIds,
        double totalPrice,
        BookingStatus bookingStatus,
        PaymentStatus paymentStatus,
        LocalDateTime createdAt,
        LocalDateTime showingStartTime,
        String theaterName

) {
}
