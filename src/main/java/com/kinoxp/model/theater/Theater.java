package com.kinoxp.model.theater;

import com.kinoxp.model.seat.Seat;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theaterId;

    private String theaterNumber;
    private int totalRows;
    private int seatsPerRow;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;

    public Theater(Long theaterId, String theaterNumber, int totalRows, int seatsPerRow, List<Seat> seats) {
        this.theaterId = theaterId;
        this.theaterNumber = theaterNumber;
        this.totalRows = totalRows;
        this.seatsPerRow = seatsPerRow;
        this.seats = new ArrayList<>(seats);
    }

    public Theater(){}

    public Long getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Long theaterId) {
        this.theaterId = theaterId;
    }

    public String getTheaterNumber() {
        return theaterNumber;
    }

    public void setTheaterNumber(String theaterName) {
        this.theaterNumber = theaterName;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(int seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = new ArrayList<>(seats);
    }
}