package com.kinoxp.model.reservation;

import com.kinoxp.model.showing.Showing;
import com.kinoxp.model.ticket.Ticket;
import com.kinoxp.model.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "showing_id")
    private Showing showing;

    private int rowNumber;
    private LocalDateTime created;
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    public Reservation(Long reservationId, User user, Showing showing, int rowNumber,
                       LocalDateTime created, double totalPrice, Status status, List<Ticket> tickets) {
        this.reservationId = reservationId;
        this.user = user;
        this.showing = showing;
        this.rowNumber = rowNumber;
        this.created = created;
        this.totalPrice = totalPrice;
        this.status = status;
        this.tickets = new ArrayList<>(tickets);
    }

    public Reservation() {}

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
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
        this.tickets = new ArrayList<>(tickets);
    }
}