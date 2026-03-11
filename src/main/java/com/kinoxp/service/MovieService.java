package com.kinoxp.service;

import com.kinoxp.dto.MovieRequest;
import com.kinoxp.dto.MovieResponse;
import com.kinoxp.model.movie.*;
import com.kinoxp.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<MovieResponse> getMovieById(Long id) {
        return movieRepository.findById(id)
                .map(this::toResponse);
    }

    public List<MovieResponse> getMoviesByGenre(Genre genre) {
        return movieRepository.findByGenre(genre)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MovieResponse> getMoviesByLanguage(Language language) {
        return movieRepository.findByLanguage(language)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MovieResponse> getMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MovieResponse createMovie(MovieRequest request) {
        Movie movie = new Movie();
        applyRequest(movie, request);
        Movie saved = movieRepository.save(movie);
        return toResponse(saved);
    }

    public Optional<MovieResponse> updateMovie(Long id, MovieRequest request) {
        return movieRepository.findById(id)
                .map(existing -> {
                    applyRequest(existing, request);
                    Movie saved = movieRepository.save(existing);
                    return toResponse(saved);
                });
    }

    public boolean deleteMovieById(Long id) {
        if (!movieRepository.existsById(id)) return false;

        movieRepository.deleteById(id);
        return true;
    }

    private void applyRequest(Movie movie, MovieRequest request) {
        movie.setTitle(request.title());
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
                movie.getLanguage()
        );
    }
}
