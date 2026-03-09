package com.kinoxp.model.showing;

import com.kinoxp.model.movie.Movie;
import com.kinoxp.model.theater.Theater;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Showing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showingId;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Showing() {}

    public Showing(Long showingId, Movie movie, Theater theater,
                   LocalDateTime startTime, LocalDateTime endTime) {
        this.showingId = showingId;
        this.movie = movie;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getShowingId() {
        return showingId;
    }

    public void setShowingId(Long showingId) {
        this.showingId = showingId;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
