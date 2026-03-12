package com.kinoxp.dto;

import com.kinoxp.model.showing.ShowingStatus;

import java.time.LocalDateTime;

public record ShowingResponse(
        Long showingId,
        Long movieId,
        String movieTitle,
        Long theaterId,
        String theaterName,
        int totalRows,
        LocalDateTime startTime,
        LocalDateTime endTime,
        ShowingStatus showingStatus
) {}
