package com.kinoxp.model.reservation;

import com.kinoxp.model.showing.Showing;
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
    @JoinColumn(name = "showing_id")
    private Showing showing;

    private String customerName;

    private LocalDateTime createdAt;

    private double totalPrice;


    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationSeat> reservedSeats = new ArrayList<>();

    public Reservation() {}

    public Reservation(Long reservationId,
                       Showing showing,
                       User user,
                       LocalDateTime createdAt,
                       double totalPrice,
                       BookingStatus bookingStatus,
                       PaymentStatus paymentStatus) {
        this.reservationId = reservationId;
        this.showing = showing;
        this.customerName = user.getName();
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.bookingStatus = bookingStatus;
        this.paymentStatus = paymentStatus;
    }


    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Showing getShowing() {
        return showing;
    }

    public void setShowing(Showing showing) {
        this.showing = showing;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<ReservationSeat> getReservedSeats() {
        return reservedSeats;
    }

    public void addReservedSeat(ReservationSeat reservationSeat) {
        reservedSeats.add(reservationSeat);
        reservationSeat.setReservation(this);
    }

    public void removeReservedSeat(ReservationSeat reservationSeat) {
        reservedSeats.remove(reservationSeat);
        reservationSeat.setReservation(null);
    }
}
