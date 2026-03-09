package com.kinoxp.dto;

public record ReservationDTO(String movieTitle, int numberOfTickets, double totalPrice, int rowNumber, Long userId, Long showingId) {}
