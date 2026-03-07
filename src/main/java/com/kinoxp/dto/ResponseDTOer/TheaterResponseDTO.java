package com.kinoxp.dto.ResponseDTOer;

public class TheaterResponseDTO {
    private Long theaterId;
    private String teatherNumber;
    private int totalRows;
    private int seatsPerRow;
    private int totaltSeats;

    private TheaterResponseDTO(){}

    public TheaterResponseDTO(Long theaterId, String teatherNumber, int totalRows, int seatsPerRow, int totaltSeats) {
        this.theaterId = theaterId;
        this.teatherNumber = teatherNumber;
        this.totalRows = totalRows;
        this.seatsPerRow = seatsPerRow;
        this.totaltSeats = totaltSeats;
    }

    public Long getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Long theaterId) {
        this.theaterId = theaterId;
    }

    public String getTeatherNumber() {
        return teatherNumber;
    }

    public void setTeatherNumber(String teatherNumber) {
        this.teatherNumber = teatherNumber;
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

    public int getTotaltSeats() {
        return totaltSeats;
    }

    public void setTotaltSeats(int totaltSeats) {
        this.totaltSeats = totaltSeats;
    }
}
