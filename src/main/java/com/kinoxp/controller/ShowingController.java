package com.kinoxp.controller;

import com.kinoxp.dto.ShowingResponse;
import com.kinoxp.service.ShowingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kino/showings")
public class ShowingController {

    private final ShowingService showingService;

    public ShowingController(ShowingService showingService) {
        this.showingService = showingService;
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<List<ShowingResponse>> getUpcomingShowingsByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showingService.getUpcomingShowingsByMovieId(movieId));
    }
}
