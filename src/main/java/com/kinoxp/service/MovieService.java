package com.kinoxp.service;

import com.kinoxp.model.movie.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private static final List<Movie> movies = List.of(
        new Movie(1,
                "Titanic",
                2023,
                Genre.ROMANCE,
                200,
                Format.THREE_DIMENSIONAL,
                AgeLimit.ALL,
                Language.DANISH),
            new Movie(2,
                    "Titanic2",
                    2024,
                    Genre.ROMANCE,
                    400,
                    Format.TWO_DIMENSIONAL,
                    AgeLimit.ALL,
                    Language.DANISH
            )
    );

    public List<Movie> getAllMovies() {
        return movies;
    }

    public Movie getMovieById(int id) {
        return movies.stream()
                .filter(movie -> movie.getMovieId() == id)
                .findFirst()
                .orElse(null);
    }
}
