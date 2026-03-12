package com.kinoxp.model.movie;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @Min(1888)
    @Max(2100)
    private int releaseYear;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Min(1)
    private int durationInMinutes;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Format format;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AgeLimit ageLimit;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Language language;

    public Movie() {}

    public Movie(Long movieId, String title, String description, int releaseYear, Genre genre,
                 int durationInMinutes, Format format, AgeLimit ageLimit, Language language) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.durationInMinutes = durationInMinutes;
        this.format = format;
        this.ageLimit = ageLimit;
        this.language = language;
        this.description = description;
    }

    public Movie(String title, int durationInMinutes, AgeLimit ageLimit) {
        this.title = title;
        this.durationInMinutes = durationInMinutes;
        this.ageLimit = ageLimit;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
