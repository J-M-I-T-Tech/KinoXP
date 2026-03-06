package com.kinoxp.model.seat;

import com.kinoxp.model.theater.Theater;
import jakarta.persistence.*;

@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seatId;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;

    private int seatNumber;
    private int rowNumber;

    public Seat(int seatId, Theater theater, int seatNumber, int rowNumber) {
        this.seatId = seatId;
        this.theater = theater;
        this.seatNumber = seatNumber;
        this.rowNumber = rowNumber;
    }
    public Seat(int seatNumber, int rowNumber) {
        this.seatNumber = seatNumber;
        this.rowNumber = rowNumber;
    }

    public Seat() {

    }


    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }
}