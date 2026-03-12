package com.kinoxp.repository;

import com.kinoxp.model.seat.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTheater_TheaterId(Long theaterId);
}
