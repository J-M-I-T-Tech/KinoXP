package com.kinoxp.controller;
import com.kinoxp.model.reservation.PriceRequest;
import com.kinoxp.model.reservation.Reservation;
import com.kinoxp.dto.ReservationRequest;
import com.kinoxp.dto.ReservationResponse;
import com.kinoxp.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
        URI location = URI.create("/api/reservations/" + createdReservation.reservationId());
        return ResponseEntity.created(location).body(createdReservation);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable Long reservationId) {
        return reservationService.getReservationById(reservationId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // TODO: metoden der beregner prisen.
    @PostMapping("/price")
    public ResponseEntity<Double> calculatePrice(@RequestBody PriceRequest request) {
        double price = reservationService.calculatePriceFromRequest(request);
        return ResponseEntity.ok(price);
    }

    //TODO: Update
//    @PutMapping

    // TODO: Slet reservation
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        boolean deleted = reservationService.deleteReservation(reservationId);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}
