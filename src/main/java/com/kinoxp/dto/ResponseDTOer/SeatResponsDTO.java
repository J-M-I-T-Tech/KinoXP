package com.kinoxp.dto.ResponseDTOer;

public class SeatResponsDTO {
    private Long seatId;
    private int rowNumber;
    private int seatNumber;

    public SeatResponsDTO(){}

    public SeatResponsDTO(Long seatId, int rowNumber, int seatNumber) {
        this.seatId = seatId;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
