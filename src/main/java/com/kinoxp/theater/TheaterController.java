package com.kinoxp.theater;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kino/theaters")
public class TheaterController {

    private final TheaterRepository theaterRepository;

    public TheaterController(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    @GetMapping
    public ResponseEntity<List<Theater>> getAllTheaters() {
        return ResponseEntity.ok(theaterRepository.findAll());
    }

    @GetMapping("/{theaterId}")
    public ResponseEntity<Theater> getTheaterById(@PathVariable Long theaterId) {
        return theaterRepository.findById(theaterId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
