package com.kinoxp.repository;

import com.kinoxp.model.reservation.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
    boolean existsByReservation_Showing_ShowingIdAndSeat_SeatId(Long showingId, Long seatId);
}
