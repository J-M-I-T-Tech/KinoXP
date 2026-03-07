package com.kinoxp.controller;

import com.kinoxp.model.reservation.Reservation;
import com.kinoxp.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("Reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping()
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservation();
    }

    //TODO lav metoden for når opretter en reservation.
//    @PostMapping("/create")
//    public double  createReservation(@RequestBody Reservation reservation){}


    // TODO: lav metoden der beregner prisen.
//    @PostMapping("/price")
//    public double CalculatePrice(@RequestBody PriceRequest request){
//
//    }


//    //TODO: Hent reservation via id
//    @GetMapping("{id]")
//    public Reservation getReservationById(@PathVariable int id) {
//
//    }


    // TODO: Hent reservation
//    @GetMapping("{id}")

    //TODO: Update
//    @PutMapping

    // TODO: Slet reservation
//    @DeleteMapping



}
