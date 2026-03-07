package com.kinoxp.dto.ResponseDTOer;

import com.kinoxp.model.reservation.Status;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationResponseDTO {
    private Long reservationId;
    private String userName;
    private String movieTitle;
    private LocalDateTime startTime;
    private int rowNumber;
    private List<Integer> seatNumbers;
    private double totalPrice;
    private Status status;

    public ReservationResponseDTO(){}

    public ReservationResponseDTO(Long reservationId, String userName, String movieTitle, LocalDateTime startTime, int rowNumber, List<Integer> seatNumbers, double totalPrice, Status status) {
        this.reservationId = reservationId;
        this.userName = userName;
        this.movieTitle = movieTitle;
        this.startTime = startTime;
        this.rowNumber = rowNumber;
        this.seatNumbers = seatNumbers;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public List<Integer> getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(List<Integer> seatNumbers) {
        this.seatNumbers = seatNumbers;
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
}
