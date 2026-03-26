package com.kinoxp.seat;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class SeatService {

    private final SeatRepository seatRepository;

    SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    List<SeatResponse> getSeatsByTheaterId(Long theaterId) {
        return seatRepository.findByTheater_TheaterId(theaterId)
                .stream()
                .map(seat -> new SeatResponse(
                        seat.getSeatId(),
                        seat.getTheater().getTheaterId(),
                        seat.getRowNumber(),
                        seat.getSeatNumber()
                ))
                .toList();
    }
}
