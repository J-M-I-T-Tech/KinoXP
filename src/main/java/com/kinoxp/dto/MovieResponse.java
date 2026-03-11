package com.kinoxp.dto;

import com.kinoxp.model.movie.AgeLimit;
import com.kinoxp.model.movie.Format;
import com.kinoxp.model.movie.Genre;
import com.kinoxp.model.movie.Language;

public record MovieResponse(
        Long movieId,
        String title,
        String Description,
        int releaseYear,
        Genre genre,
        int durationInMinutes,
        Format format,
        AgeLimit ageLimit,
        Language language
) {
}
