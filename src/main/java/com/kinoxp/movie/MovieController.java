package com.kinoxp.movie;

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

    @GetMapping("/create")
    public ResponseEntity<Void> openCreateMoviePage() {
        return ResponseEntity.status(302)
                .location(URI.create("/html/index.html?openCreateMovie=true"))
                .build();
    }

    @GetMapping("/{movieId:\\d+}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long movieId) {
        return movieService.getMovieById(movieId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest request) {
        MovieResponse created = movieService.createMovie(request);
        return ResponseEntity.created(URI.create("/kino/movies/" + created.movieId())).body(created);
    }

    @PostMapping("/create")
    public ResponseEntity<MovieResponse> createMovieFromCreatePath(@Valid @RequestBody MovieRequest request) {
        return createMovie(request);
    }

    @PutMapping("/{movieId:\\d+}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long movieId,
                                                     @Valid @RequestBody MovieRequest request) {
        return movieService.updateMovie(movieId, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{movieId:\\d+}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
        return movieService.deleteMovieById(movieId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}