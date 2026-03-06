package com.kinoxp.model.movie;

public class Movie {
    private int movieId;
    private String title;
    private int releaseYear;
    private Genre genre;
    private int durationInMinutes;
    private Format format;
    private AgeLimit ageLimit;
    private Language languge;

    public Movie(int movieId, String title, int releaseYear, Genre genre,
                 int durationInMinutes, Format format, AgeLimit ageLimit, Language languge) {
        this.movieId = movieId;
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.durationInMinutes = durationInMinutes;
        this.format = format;
        this.ageLimit = ageLimit;
        this.languge = languge;
    }

    // constructor til test US:3.6
    public Movie(String title, int durationInMinutes, AgeLimit ageLimit) {
        this.title = title;
        this.durationInMinutes = durationInMinutes;
        this.ageLimit = ageLimit;
    }



    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

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

    public Language getLanguge() {
        return languge;
    }

    public void setLanguge(Language languge) {
        this.languge = languge;
    }
}
