package com.kinoxp.reservation;

import com.kinoxp.seat.Seat;
import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"reservation_id", "seat_id"}))
public class ReservationSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationSeatId;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    private double price;

    public ReservationSeat() {}

    public ReservationSeat(Long reservationSeatId, Reservation reservation, Seat seat, double price) {
        this.reservationSeatId = reservationSeatId;
        this.reservation = reservation;
        this.seat = seat;
        this.price = price;
    }

    public Long getReservationSeatId() { return reservationSeatId; }
    public void setReservationSeatId(Long reservationSeatId) { this.reservationSeatId = reservationSeatId; }

    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }

    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
