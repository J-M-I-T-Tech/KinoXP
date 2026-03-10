package com.kinoxp.model.movie;

public record MovieResponse(
        Long movieId,
        String title,
        int releaseYear,
        Genre genre,
        int durationInMinutes,
        Format format,
        AgeLimit ageLimit,
        Language language
) {
}
