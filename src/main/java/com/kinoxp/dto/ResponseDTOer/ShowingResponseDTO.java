package com.kinoxp.dto.ResponseDTOer;

import com.kinoxp.model.movie.AgeLimit;
import com.kinoxp.model.movie.Format;
import com.kinoxp.model.movie.Genre;
import com.kinoxp.model.movie.Language;

import java.time.LocalDateTime;

public class ShowingResponseDTO {
    private Long showingId;
    private String movieTitle;
    private int releaseYear;
    private Genre genre;
    private String theaterName;
    private LocalDateTime startTime;
    private int durationInMinutes;
    private Format format;
    private Language language;
    private AgeLimit ageLimit;

    private ShowingResponseDTO(){}

    public ShowingResponseDTO(Long showingId, String movieTitle, int releaseYear, Genre genre, String theaterName, LocalDateTime startTime, int durationInMinutes, Format format, Language language, AgeLimit ageLimit) {
        this.showingId = showingId;
        this.movieTitle = movieTitle;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.theaterName = theaterName;
        this.startTime = startTime;
        this.durationInMinutes = durationInMinutes;
        this.format = format;
        this.language = language;
        this.ageLimit = ageLimit;
    }

    public Long getShowingId() {
        return showingId;
    }

    public void setShowingId(Long showingId) {
        this.showingId = showingId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
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

    public String getTheaterName() {
        return theaterName;
    }

    public void setTheaterName(String theaterName) {
        this.theaterName = theaterName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public AgeLimit getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(AgeLimit ageLimit) {
        this.ageLimit = ageLimit;
    }
}
