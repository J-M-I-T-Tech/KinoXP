package com.kinoxp.controller;

import com.kinoxp.dto.ReservationDTO;
import com.kinoxp.model.reservation.Reservation;
import com.kinoxp.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    //TODO lav metoden for når opretter en reservation.
    @PostMapping("/create")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO){
        ReservationDTO savedReservation = reservationService.createReservation(reservationDTO);
        return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(reservationService.convertToDTO(reservation));
    }

    // TODO: lav metoden der beregner prisen.
//    @PostMapping("/price")
//    public double CalculatePrice(@RequestBody PriceRequest request){
//
//    }

    //TODO: Update
//    @PutMapping

    // TODO: Slet reservation
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
