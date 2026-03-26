package com.kinoxp.showing;

import com.kinoxp.movie.Movie;
import com.kinoxp.theater.Theater;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Showing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showingId;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private ShowingStatus showingStatus;

    public Showing() {}

    public Showing(Long showingId, Movie movie, Theater theater,
                   LocalDateTime startTime, LocalDateTime endTime, ShowingStatus showingStatus) {
        this.showingId = showingId;
        this.movie = movie;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
        this.showingStatus = showingStatus;
    }

    public Long getShowingId() { return showingId; }
    public void setShowingId(Long showingId) { this.showingId = showingId; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public Theater getTheater() { return theater; }
    public void setTheater(Theater theater) { this.theater = theater; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public ShowingStatus getShowingStatus() { return showingStatus; }
    public void setShowingStatus(ShowingStatus showingStatus) { this.showingStatus = showingStatus; }
}
