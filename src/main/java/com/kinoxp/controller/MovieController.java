package com.kinoxp.controller;

import com.kinoxp.model.movie.Movie;
import com.kinoxp.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("kino")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<Movie> getMovieById(@PathVariable int movieId) {
        Movie movie = movieService.getMovieById(movieId);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/movies/{movieId}/delete")
    public ResponseEntity<Void> deleteMovie(@PathVariable int movieId) {
        Movie movie = movieService.getMovieById(movieId);
        if (movie != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
