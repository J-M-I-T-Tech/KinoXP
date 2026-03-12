package com.kinoxp.repository;

import com.kinoxp.model.reservation.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
    boolean existsByReservation_Showing_ShowingIdAndSeat_SeatId(Long showingId, Long seatId);
    List<ReservationSeat> findByReservation_Showing_ShowingId(Long showingId);
}
