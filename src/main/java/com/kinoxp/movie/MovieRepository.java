package com.kinoxp.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenre(Genre genre);
    List<Movie> findByLanguage(Language language);
    List<Movie> findByTitleContainingIgnoreCase(String title);
}
