package com.kinoxp.dto.CreateDTOer;

import com.kinoxp.model.movie.AgeLimit;
import com.kinoxp.model.movie.Format;
import com.kinoxp.model.movie.Genre;
import com.kinoxp.model.movie.Language;

public class MovieCreateDTO {
    private String title;
    private int releaseYear;
    private Genre genre;
    private int durationInMinutes;
    private Format format;
    private AgeLimit ageLimit;
    private Language language;

    private MovieCreateDTO(){}


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public AgeLimit getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(AgeLimit ageLimit) {
        this.ageLimit = ageLimit;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
