package com.kinoxp.model.reservation;

import com.kinoxp.model.showing.Showing;
import com.kinoxp.model.ticket.Ticket;
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

        public Reservation(int reservationID, User user, Showing showing, int rowNumber,
                            LocalDateTime created, double totalPrice, Status status, List<Ticket> tickets) {
            this.reservationID = reservationID;
            this.user = user;
            this.showing = showing;
            this.rowNumber = rowNumber;
            this.created = created;
            this.totalPrice = totalPrice;
            this.status = status;
            this.tickets = tickets;
        }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Showing getShowing() {
        return showing;
    }

    public void setShowing(Showing showing) {
        this.showing = showing;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}

