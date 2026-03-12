package com.kinoxp.controller;

import com.kinoxp.dto.SeatResponse;
import com.kinoxp.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
