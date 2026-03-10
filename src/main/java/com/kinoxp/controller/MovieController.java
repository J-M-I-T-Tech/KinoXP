package com.kinoxp.controller;

import com.kinoxp.dto.MovieRequest;
import com.kinoxp.dto.MovieResponse;
import com.kinoxp.model.movie.*;
import com.kinoxp.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/kino/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieResponse>> getMoviesByGenre(@PathVariable Genre genre) {
        return ResponseEntity.ok(movieService.getMoviesByGenre(genre));
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<List<MovieResponse>> getMoviesByLanguage(@PathVariable Language language) {
        return ResponseEntity.ok(movieService.getMoviesByLanguage(language));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieResponse>> getMoviesByTitle(@RequestParam String title) {
        return ResponseEntity.ok(movieService.getMoviesByTitle(title));
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long movieId) {
        return movieService.getMovieById(movieId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest request) {
        MovieResponse createdMovie = movieService.createMovie(request);
        URI location = URI.create("/kino/movies/" + createdMovie.movieId());
        return ResponseEntity.created(location).body(createdMovie);
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long movieId, @Valid @RequestBody MovieRequest request) {
        return movieService.updateMovie(movieId, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
        boolean deleted = movieService.deleteMovieById(movieId);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}
