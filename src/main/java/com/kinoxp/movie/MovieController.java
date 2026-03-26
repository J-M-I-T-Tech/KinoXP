package com.kinoxp.movie;

import com.kinoxp.shared.AdminChecker;
import com.kinoxp.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/kino/movies")
public class MovieController {

    private final MovieService movieService;
    private final UserService userService;

    public MovieController(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
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

    @PostMapping("/create")
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest request, @RequestParam Long userId) {
        if (!AdminChecker.isAdmin(userService, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        MovieResponse created = movieService.createMovie(request);
        return ResponseEntity.created(URI.create("/kino/movies/" + created.movieId())).body(created);
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long movieId,
                                                     @Valid @RequestBody MovieRequest request,
                                                     @RequestParam Long userId) {
        if (!AdminChecker.isAdmin(userService, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return movieService.updateMovie(movieId, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId, @RequestParam Long userId) {
        if (!AdminChecker.isAdmin(userService, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return movieService.deleteMovieById(movieId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
