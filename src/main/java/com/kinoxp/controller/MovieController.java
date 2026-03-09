package com.kinoxp.controller;

import com.kinoxp.dto.MovieRequest;
import com.kinoxp.dto.MovieResponse;
import com.kinoxp.model.movie.Genre;
import com.kinoxp.model.movie.Language;
import com.kinoxp.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("kino")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<List<MovieResponse>> getMoviesByGenre(@PathVariable Genre genre) {
        return ResponseEntity.ok(movieService.getMoviesByGenre(genre));
    }

    public ResponseEntity<List<MovieResponse>> getMoviesByLanguage(@PathVariable Language language) {
        return ResponseEntity.ok(movieService.getMoviesByLanguage(language));
    }

    public ResponseEntity<List<MovieResponse>> getMoviesByTitle(@RequestParam String title) {
        return ResponseEntity.ok(movieService.getMoviesByTitle(title));
    }

    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long movieId) {
        return movieService.getMovieById(movieId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/movies")
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest request) {
        MovieResponse createdMovie = movieService.createMovie(request);
        URI location = URI.create("/kino/movies/" + createdMovie.movieId());
        return ResponseEntity.created(location).body(createdMovie);
    }

    @PutMapping("/movies/{movieId}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long movieId, @Valid @RequestBody MovieRequest request) {
        return movieService.updateMovie(movieId, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/movies/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
        boolean deleted = movieService.deleteMovieById(movieId);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}
