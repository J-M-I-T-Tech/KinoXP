package com.kinoxp.model.theater;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kinoxp.model.seat.Seat;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theaterId;

    private String theaterName;
    private int totalRows;
    private int seatsPerRow;

    @JsonIgnore
    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();

    public Theater() {}

    public Theater(Long theaterId, String theaterName, int totalRows, int seatsPerRow) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.totalRows = totalRows;
        this.seatsPerRow = seatsPerRow;
    }

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

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(int seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public int getTotalCapacity() {
        return totalRows * seatsPerRow;
    }
}