package com.kinoxp.repository;

import com.kinoxp.model.movie.Genre;
import com.kinoxp.model.movie.Language;
import com.kinoxp.model.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenre(Genre genre);

    List<Movie> findByLanguage(Language language);

    List<Movie> findByTitleContainingIgnoreCase(String title);
}
