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

    private LocalDateTime created;

    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingState bookingState;

    @Enumerated(EnumType.STRING)
    private PaymentState paymentState;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    public Reservation(Long reservationId, User user, Showing showing, LocalDateTime created,
                       double totalPrice, BookingState bookingState, PaymentState paymentState, List<Ticket> tickets) {
        this.reservationId = reservationId;
        this.user = user;
        this.showing = showing;
        this.created = created;
        this.totalPrice = totalPrice;
        this.bookingState = bookingState;
        this.paymentState = paymentState;
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

    public BookingState getBookingState() {
        return bookingState;
    }

    public void setBookingState(BookingState bookingState) {
        this.bookingState = bookingState;
    }

    public PaymentState getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = new ArrayList<>(tickets);
    }
}
