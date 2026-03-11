package com.kinoxp.service;

import com.kinoxp.dto.ShowingResponse;
import com.kinoxp.model.showing.Showing;
import com.kinoxp.repository.ShowingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShowingService {

    private final ShowingRepository showingRepository;

    public ShowingService(ShowingRepository showingRepository) {
        this.showingRepository = showingRepository;
    }

    public List<ShowingResponse> getUpcomingShowingsByMovieId(Long movieId) {
        return showingRepository.findByMovie_MovieIdAndStartTimeAfterOrderByStartTimeAsc(movieId, LocalDateTime.now())
                .stream()
                .filter(showing -> showing.getShowingStatus() == com.kinoxp.model.showing.ShowingStatus.UPCOMING)
                .map(this::toResponse)
                .toList();
    }

    private ShowingResponse toResponse(Showing showing) {
        return new ShowingResponse(
                showing.getShowingId(),
                showing.getMovie().getMovieId(),
                showing.getMovie().getTitle(),
                showing.getTheater().getTheaterId(),
                showing.getTheater().getTheaterName(),
                showing.getTheater().getTotalRows(),
                showing.getStartTime(),
                showing.getEndTime(),
                showing.getShowingStatus()
        );
    }
}
