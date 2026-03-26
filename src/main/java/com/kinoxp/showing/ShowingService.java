package com.kinoxp.showing;

import com.kinoxp.movie.Movie;
import com.kinoxp.movie.MovieRepository;
import com.kinoxp.theater.Theater;
import com.kinoxp.theater.TheaterRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
class ShowingService {

    private final ShowingRepository showingRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    ShowingService(ShowingRepository showingRepository,
                   MovieRepository movieRepository,
                   TheaterRepository theaterRepository) {
        this.showingRepository = showingRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
    }

    List<ShowingResponse> getUpcomingShowingsByMovieId(Long movieId) {
        return showingRepository
                .findByMovie_MovieIdAndStartTimeAfterOrderByStartTimeAsc(movieId, LocalDateTime.now())
                .stream()
                .filter(s -> s.getShowingStatus() == ShowingStatus.UPCOMING)
                .map(this::toResponse)
                .toList();
    }

    List<ShowingResponse> getAllShowingsByMovieId(Long movieId) {
        return showingRepository.findByMovie_MovieIdOrderByStartTimeAsc(movieId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    ShowingResponse createShowing(ShowingRequest request) {
        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new RuntimeException("Film ikke fundet"));
        Theater theater = theaterRepository.findById(request.theaterId())
                .orElseThrow(() -> new RuntimeException("Sal ikke fundet"));

        Showing showing = new Showing();
        showing.setMovie(movie);
        showing.setTheater(theater);
        showing.setStartTime(request.startTime());
        showing.setEndTime(request.endTime());
        showing.setShowingStatus(ShowingStatus.valueOf(request.showingStatus()));

        return toResponse(showingRepository.save(showing));
    }

    Optional<ShowingResponse> updateShowing(Long showingId, ShowingRequest request) {
        return showingRepository.findById(showingId).map(showing -> {
            Movie movie = movieRepository.findById(request.movieId())
                    .orElseThrow(() -> new RuntimeException("Film ikke fundet"));
            Theater theater = theaterRepository.findById(request.theaterId())
                    .orElseThrow(() -> new RuntimeException("Sal ikke fundet"));

            showing.setMovie(movie);
            showing.setTheater(theater);
            showing.setStartTime(request.startTime());
            showing.setEndTime(request.endTime());
            showing.setShowingStatus(ShowingStatus.valueOf(request.showingStatus()));

            return toResponse(showingRepository.save(showing));
        });
    }

    boolean deleteShowing(Long showingId) {
        if (!showingRepository.existsById(showingId)) return false;
        showingRepository.deleteById(showingId);
        return true;
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
