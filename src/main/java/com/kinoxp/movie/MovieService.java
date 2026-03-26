package com.kinoxp.movie;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class MovieService {

    private final MovieRepository movieRepository;

    MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    List<MovieResponse> getAllMovies() {
        return movieRepository.findAll().stream().map(this::toResponse).toList();
    }

    Optional<MovieResponse> getMovieById(Long id) {
        return movieRepository.findById(id).map(this::toResponse);
    }

    List<MovieResponse> getMoviesByGenre(Genre genre) {
        return movieRepository.findByGenre(genre).stream().map(this::toResponse).toList();
    }

    List<MovieResponse> getMoviesByLanguage(Language language) {
        return movieRepository.findByLanguage(language).stream().map(this::toResponse).toList();
    }

    List<MovieResponse> getMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title).stream().map(this::toResponse).toList();
    }

    MovieResponse createMovie(MovieRequest request) {
        Movie movie = new Movie();
        applyRequest(movie, request);
        return toResponse(movieRepository.save(movie));
    }

    Optional<MovieResponse> updateMovie(Long id, MovieRequest request) {
        return movieRepository.findById(id).map(existing -> {
            applyRequest(existing, request);
            return toResponse(movieRepository.save(existing));
        });
    }

    boolean deleteMovieById(Long id) {
        if (!movieRepository.existsById(id)) return false;
        movieRepository.deleteById(id);
        return true;
    }

    private void applyRequest(Movie movie, MovieRequest request) {
        movie.setTitle(request.title());
        movie.setDescription(request.description());
        movie.setReleaseYear(request.releaseYear());
        movie.setGenre(request.genre());
        movie.setDurationInMinutes(request.durationInMinutes());
        movie.setFormat(request.format());
        movie.setAgeLimit(request.ageLimit());
        movie.setLanguage(request.language());
    }

    private MovieResponse toResponse(Movie movie) {
        return new MovieResponse(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getReleaseYear(),
                movie.getGenre(),
                movie.getDurationInMinutes(),
                movie.getFormat(),
                movie.getAgeLimit(),
                movie.getLanguage(),
                movie.getDescription()
        );
    }
}
