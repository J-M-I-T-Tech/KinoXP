package com.kinoxp.dto.CreateDTOer;

import java.time.LocalDateTime;

public class ShowingCreateDTO {
    private Long movieId;
    private Long theaterId;
    private LocalDateTime startTime;

    public ShowingCreateDTO(){}

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Long theaterId) {
        this.theaterId = theaterId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
