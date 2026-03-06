package com.kinoxp.repository;

import com.kinoxp.model.seat.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository <Seat,Long> {
}
