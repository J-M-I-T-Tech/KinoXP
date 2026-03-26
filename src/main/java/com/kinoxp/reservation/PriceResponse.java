package com.kinoxp.reservation;

import java.util.List;

public record PriceResponse(
        double totalPrice,
        List<PriceBreakdownItem> breakdown
) {
    public record PriceBreakdownItem(String label, double amount) {}
}
