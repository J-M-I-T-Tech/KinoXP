package com.kinoxp.showing;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kino/showings")
public class ShowingController {

    private final ShowingService showingService;

    public ShowingController(ShowingService showingService) {
        this.showingService = showingService;
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowingResponse>> getUpcomingShowingsByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showingService.getUpcomingShowingsByMovieId(movieId));
    }

    @GetMapping("/movie/{movieId}/all")
    public ResponseEntity<List<ShowingResponse>> getAllShowingsByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showingService.getAllShowingsByMovieId(movieId));
    }

    @PostMapping
    public ResponseEntity<ShowingResponse> createShowing(@RequestBody ShowingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(showingService.createShowing(request));
    }

    @PutMapping("/{showingId}")
    public ResponseEntity<ShowingResponse> updateShowing(@PathVariable Long showingId,
                                                         @RequestBody ShowingRequest request) {
        return showingService.updateShowing(showingId, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{showingId}")
    public ResponseEntity<Void> deleteShowing(@PathVariable Long showingId) {
        return showingService.deleteShowing(showingId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
