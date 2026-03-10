package com.kinoxp.service;

import com.kinoxp.model.reservation.*;
import com.kinoxp.model.seat.Seat;
import com.kinoxp.model.showing.Showing;
import com.kinoxp.repository.ReservationRepository;
import com.kinoxp.repository.ReservationSeatRepository;
import com.kinoxp.repository.SeatRepository;
import com.kinoxp.repository.ShowingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private static final double DEFAULT_TICKET_PRICE = 100;

    private final ReservationRepository reservationRepository;
    private final ShowingRepository showingRepository;
    private final SeatRepository seatRepository;
    private final ReservationSeatRepository reservationSeatRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ShowingRepository showingRepository,
                              SeatRepository seatRepository,
                              ReservationSeatRepository reservationSeatRepository) {
        this.reservationRepository = reservationRepository;
        this.showingRepository = showingRepository;
        this.seatRepository = seatRepository;
        this.reservationSeatRepository = reservationSeatRepository;
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<ReservationResponse> getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .map(this::toResponse);
    }

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        Showing showing = showingRepository.findById(request.showingId())
                .orElseThrow(() -> new RuntimeException("Showing not found"));

        List<Seat> seats = seatRepository.findAllById(request.seatIds());

        if (seats.size() != request.seatIds().size()) {
            throw new RuntimeException("One or more seats were not found");
        }

        for (Seat seat : seats) {
            if (!seat.getTheater().getTheaterId().equals(showing.getTheater().getTheaterId())) {
                throw new RuntimeException("Seat does not belong to the showing's theater");
            }

            boolean alreadyBooked = reservationSeatRepository
                    .existsByReservation_Showing_ShowingIdAndSeat_SeatId(showing.getShowingId(), seat.getSeatId());

            if (alreadyBooked) {
                throw new RuntimeException(
                        "Seat already booked for this showing: row " + seat.getRowNumber() + ", seat " + seat.getSeatNumber()
                );
            }
        }

        Reservation reservation = new Reservation();
        reservation.setShowing(showing);
        reservation.setCustomerName(request.customerName());
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setBookingStatus(BookingStatus.CONFIRMED);
        reservation.setPaymentStatus(PaymentStatus.AWAITING);

        reservation.setTotalPrice(DEFAULT_TICKET_PRICE * seats.size());

        for (Seat seat : seats) {
            ReservationSeat reservationSeat = new ReservationSeat();
            reservationSeat.setSeat(seat);
            reservationSeat.setPrice(DEFAULT_TICKET_PRICE);
            reservation.addReservedSeat(reservationSeat);
        }

        Reservation savedReservation = reservationRepository.save(reservation);
        return toResponse(savedReservation);
    }

    public boolean deleteReservation(Long reservationId) {
        if (!reservationRepository.existsById(reservationId)) return false;

        reservationRepository.deleteById(reservationId);
        return true;
    }

    private ReservationResponse toResponse(Reservation reservation) {
        List<Long> seatIds = reservation.getReservedSeats()
                .stream()
                .map(reservationSeat -> reservationSeat.getSeat().getSeatId())
                .toList();

        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getShowing().getShowingId(),
                reservation.getShowing().getMovie().getTitle(),
                reservation.getCustomerName(),
                seatIds,
                reservation.getTotalPrice(),
                reservation.getBookingStatus(),
                reservation.getPaymentStatus(),
                reservation.getCreatedAt()
        );
    }
}
