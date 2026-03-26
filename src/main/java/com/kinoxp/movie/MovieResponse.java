package com.kinoxp.movie;

public record MovieResponse(
        Long movieId,
        String title,
        String Description,
        int releaseYear,
        Genre genre,
        int durationInMinutes,
        Format format,
        AgeLimit ageLimit,
        Language language,
        String description
) {}
