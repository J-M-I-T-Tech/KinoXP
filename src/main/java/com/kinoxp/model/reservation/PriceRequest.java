package com.kinoxp.model.reservation;


public class PriceRequest {

    private Long showingId;
    private int numberOfTickets;
    private int rowNumber;

    public PriceRequest() {
    }

    public PriceRequest(Long showingId, int numberOfTickets, int rowNumber) {
        this.showingId = showingId;
        this.numberOfTickets = numberOfTickets;
        this.rowNumber = rowNumber;
    }

    public Long getShowingId() {
        return showingId;
    }

    public void setShowingId(Long showingId) {
        this.showingId = showingId;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }
}