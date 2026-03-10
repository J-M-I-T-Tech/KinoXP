package com.kinoxp.dto;

import com.kinoxp.model.movie.AgeLimit;
import com.kinoxp.model.movie.Format;
import com.kinoxp.model.movie.Genre;
import com.kinoxp.model.movie.Language;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovieRequest(
        @NotBlank(message = "Title is required")
        String title,

        @Min(value = 1888, message = "Release year must be >= 1888")
        @Max(value = 2100, message = "Release year must be <= 2100")
        int releaseYear,

        @NotNull(message = "Genre is required")
        Genre genre,

        @Min(value = 1, message = "Duration must be at least 1 minute")
        int durationInMinutes,

        @NotNull(message = "Format is required")
        Format format,

        @NotNull(message = "Age limit is required")
        AgeLimit ageLimit,

        @NotNull(message = "Language is required")
        Language language
) {
}
