package com.kinoxp.controller;

import com.kinoxp.dto.PriceResponse;
import com.kinoxp.dto.ReservationRequest;
import com.kinoxp.dto.ReservationResponse;
import com.kinoxp.model.reservation.PriceRequest;
import com.kinoxp.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("kino/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest request) {
        ReservationResponse createdReservation = reservationService.createReservation(request);
        URI location = URI.create("/kino/reservations/" + createdReservation.reservationId());
        return ResponseEntity.created(location).body(createdReservation);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable Long reservationId) {
        return reservationService.getReservationById(reservationId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByCustomer(@PathVariable String customerName) {
        return ResponseEntity.ok(reservationService.getReservationsByCustomerName(customerName));
    }

    @GetMapping("/showing/{showingId}/reserved-seats")
    public ResponseEntity<List<Long>> getReservedSeatIdsByShowing(@PathVariable Long showingId) {
        return ResponseEntity.ok(reservationService.getReservedSeatIdsByShowingId(showingId));
    }

    // TODO: metoden der beregner prisen.
    @PostMapping("/price")
    public ResponseEntity<PriceResponse> calculatePrice(@RequestBody PriceRequest request) {
        PriceResponse priceResponse = reservationService.calculatePriceFromRequest(request);
        return ResponseEntity.ok(priceResponse);
    }

    // TODO: Slet reservation
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        boolean deleted = reservationService.deleteReservation(reservationId);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }

    //TODO: US 2.3 - Rediger reservation (medarbejder)

    // @PutMapping

    // @PatchMapping

}
