package com.kinoxp.model.reservation;

import com.kinoxp.model.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Reservation {
    private int reservationID;
    private User user;
    private Showing showing;
    private int rowNumber;
    private LocalDateTime created;
    private double totalPrice;
    private Status status;
    private List<Ticket> tickets;
}
