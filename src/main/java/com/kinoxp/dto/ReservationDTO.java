package com.kinoxp.dto;

public record ReservationDTO(String movieTitle, int numberOfTickets, double totalPrice, Long userId, Long showingId) {}
