package com.kinoxp.showing;

import java.time.LocalDateTime;

public record ShowingRequest(
        Long movieId,
        Long theaterId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String showingStatus
) {}
