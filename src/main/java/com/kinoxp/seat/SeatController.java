package com.kinoxp.seat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kino/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<SeatResponse>> getSeatsByTheaterId(@PathVariable Long theaterId) {
        return ResponseEntity.ok(seatService.getSeatsByTheaterId(theaterId));
    }
}
