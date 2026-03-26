package com.kinoxp.reservation;

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
        ReservationResponse created = reservationService.createReservation(request);
        return ResponseEntity.created(URI.create("/kino/reservations/" + created.reservationId())).body(created);
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

    @PostMapping("/price")
    public ResponseEntity<PriceResponse> calculatePrice(@RequestBody PriceRequest request) {
        return ResponseEntity.ok(reservationService.calculatePriceFromRequest(request));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        return reservationService.deleteReservation(reservationId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
