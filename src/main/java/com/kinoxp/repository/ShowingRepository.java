package com.kinoxp.repository;

import com.kinoxp.model.showing.Showing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowingRepository extends JpaRepository<Showing, Long> {
    List<Showing> findByMovie_MovieIdAndStartTimeAfterOrderByStartTimeAsc(Long movieId, LocalDateTime startTime);
}
