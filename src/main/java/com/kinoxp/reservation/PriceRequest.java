package com.kinoxp.reservation;

import java.util.List;

public class PriceRequest {
    private Long showingId;
    private List<Long> seatIds;

    public PriceRequest() {}

    public PriceRequest(Long showingId, List<Long> seatIds) {
        this.showingId = showingId;
        this.seatIds = seatIds;
    }

    public Long getShowingId() { return showingId; }
    public void setShowingId(Long showingId) { this.showingId = showingId; }

    public List<Long> getSeatIds() { return seatIds; }
    public void setSeatIds(List<Long> seatIds) { this.seatIds = seatIds; }
}
