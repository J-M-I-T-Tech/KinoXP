package com.kinoxp.model.theater;

import com.kinoxp.model.seat.Seat;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theaterId;

    private String theaterName;
    private int totalRows;
    private int totalSeats;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;

    public Theater(Long theaterId, String theaterName, int totalRows, int totalSeats, List<Seat> seats) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.totalRows = totalRows;
        this.totalSeats = totalSeats;
        this.seats = seats;
    }

    public Theater(){}

    public Long getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Long theaterId) {
        this.theaterId = theaterId;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public void setTheaterName(String theaterName) {
        this.theaterName = theaterName;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}