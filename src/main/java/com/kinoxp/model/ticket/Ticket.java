package com.kinoxp.model.ticket;

import com.kinoxp.model.reservation.Reservation;

public class Ticket {
    private int ticketID;
    private Reservation reservation;
    private Seat seat;
    private double price;

    public Ticket(int ticketID, Reservation reservation, Seat seat, double price) {
        this.ticketID = ticketID;
        this.reservation = reservation;
        this.seat = seat;
        this.price = price;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

