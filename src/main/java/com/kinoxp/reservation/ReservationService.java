package com.kinoxp.reservation;

import com.kinoxp.movie.Format;
import com.kinoxp.movie.Movie;
import com.kinoxp.seat.Seat;
import com.kinoxp.seat.SeatRepository;
import com.kinoxp.showing.Showing;
import com.kinoxp.showing.ShowingRepository;
import com.kinoxp.user.User;
import com.kinoxp.user.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
class ReservationService {

    private static final double STANDARD_TICKET_PRICE = 130.0;
    private static final double LONG_MOVIE_FEE = 20.0;
    private static final double THREE_D_FEE = 30.0;
    private static final double COWBOY_ROW_DISCOUNT = 15.0;
    private static final double SOFA_ROW_FEE = 25.0;
    private static final double RESERVATION_FEE = 5.0;
    private static final double GROUP_DISCOUNT_THRESHOLD = 10;
    private static final double GROUP_DISCOUNT_RATE = 0.07;

    private final ReservationRepository reservationRepository;
    private final ShowingRepository showingRepository;
    private final SeatRepository seatRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final UserRepository userRepository;

    ReservationService(ReservationRepository reservationRepository,
                       ShowingRepository showingRepository,
                       SeatRepository seatRepository,
                       ReservationSeatRepository reservationSeatRepository,
                       UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.showingRepository = showingRepository;
        this.seatRepository = seatRepository;
        this.reservationSeatRepository = reservationSeatRepository;
        this.userRepository = userRepository;
    }

    List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream().map(this::toResponse).toList();
    }

    Optional<ReservationResponse> getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).map(this::toResponse);
    }

    @Transactional
    ReservationResponse createReservation(@Valid ReservationRequest request) {
        Showing showing = showingRepository.findById(request.showingId())
                .orElseThrow(() -> new RuntimeException("Showing not found"));

        List<Seat> seats = seatRepository.findAllById(request.seatIds());
        if (seats.size() != request.seatIds().size()) {
            throw new RuntimeException("One or more seats were not found");
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("Access denied"));
        if (user.getRole() == null) {
            throw new RuntimeException("Access denied");
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
        reservation.setTotalPrice(calculateTotalPrice(showing, seats));

        for (Seat seat : seats) {
            ReservationSeat rs = new ReservationSeat();
            rs.setSeat(seat);
            rs.setPrice(calculateSeatPrice(showing, seat));
            reservation.addReservedSeat(rs);
        }

        return toResponse(reservationRepository.save(reservation));
    }

    boolean deleteReservation(Long reservationId) {
        if (!reservationRepository.existsById(reservationId)) return false;
        reservationRepository.deleteById(reservationId);
        return true;
    }

    List<ReservationResponse> getReservationsByCustomerName(String customerName) {
        return reservationRepository.findByCustomerName(customerName).stream().map(this::toResponse).toList();
    }

    List<Long> getReservedSeatIdsByShowingId(Long showingId) {
        return reservationSeatRepository.findByReservation_Showing_ShowingId(showingId)
                .stream()
                .map(rs -> rs.getSeat().getSeatId())
                .toList();
    }

    PriceResponse calculatePriceFromRequest(PriceRequest request) {
        Showing showing = showingRepository.findById(request.getShowingId())
                .orElseThrow(() -> new RuntimeException("Showing not found"));
        List<Seat> seats = seatRepository.findAllById(request.getSeatIds());
        return calculateFullPriceDetails(showing, seats);
    }

    double calculateSeatPrice(Showing showing, Seat seat) {
        double price = STANDARD_TICKET_PRICE;
        Movie movie = showing.getMovie();

        if (seat.getRowNumber() <= 2) {
            price -= COWBOY_ROW_DISCOUNT;
        } else if (showing.getTheater().getTotalRows() > 15 &&
                seat.getRowNumber() > showing.getTheater().getTotalRows() - 2) {
            price += SOFA_ROW_FEE;
        }

        if (movie.getDurationInMinutes() > 170) price += LONG_MOVIE_FEE;
        if (movie.getFormat() == Format.FORMAT_3D) price += THREE_D_FEE;

        return price;
    }

    double calculateTotalPrice(Showing showing, List<Seat> seats) {
        double total = seats.stream().mapToDouble(seat -> calculateSeatPrice(showing, seat)).sum();

        if (seats.size() > GROUP_DISCOUNT_THRESHOLD) {
            total -= (STANDARD_TICKET_PRICE * seats.size()) * GROUP_DISCOUNT_RATE;
        }
        if (seats.size() <= 5) {
            total += RESERVATION_FEE;
        }

        return Math.round(total * 100) / 100.0;
    }

    PriceResponse calculateFullPriceDetails(Showing showing, List<Seat> seats) {
        double total = 0;
        List<PriceResponse.PriceBreakdownItem> breakdown = new ArrayList<>();

        int cowboySeats = 0, sofaSeats = 0, standardSeats = 0;
        Movie movie = showing.getMovie();
        boolean isLongFilm = movie.getDurationInMinutes() > 170;
        boolean is3D = movie.getFormat() == Format.FORMAT_3D;

        for (Seat seat : seats) {
            double base = STANDARD_TICKET_PRICE;
            if (seat.getRowNumber() <= 2) { base -= COWBOY_ROW_DISCOUNT; cowboySeats++; }
            else if (showing.getTheater().getTotalRows() > 15 &&
                    seat.getRowNumber() > showing.getTheater().getTotalRows() - 2) { base += SOFA_ROW_FEE; sofaSeats++; }
            else { standardSeats++; }

            if (isLongFilm) base += LONG_MOVIE_FEE;
            if (is3D) base += THREE_D_FEE;
            total += base;
        }

        if (standardSeats > 0) breakdown.add(new PriceResponse.PriceBreakdownItem("Standard billetter (" + standardSeats + " stk)", standardSeats * STANDARD_TICKET_PRICE));
        if (cowboySeats > 0) breakdown.add(new PriceResponse.PriceBreakdownItem("Cowboy-rækker (" + cowboySeats + " stk)", cowboySeats * (STANDARD_TICKET_PRICE - COWBOY_ROW_DISCOUNT)));
        if (sofaSeats > 0) breakdown.add(new PriceResponse.PriceBreakdownItem("Sofa-rækker (" + sofaSeats + " stk)", sofaSeats * (STANDARD_TICKET_PRICE + SOFA_ROW_FEE)));
        if (isLongFilm) breakdown.add(new PriceResponse.PriceBreakdownItem("Tillæg: Helaftensfilm", seats.size() * LONG_MOVIE_FEE));
        if (is3D) breakdown.add(new PriceResponse.PriceBreakdownItem("Tillæg: 3D-film", seats.size() * THREE_D_FEE));

        if (seats.size() > GROUP_DISCOUNT_THRESHOLD) {
            double discount = (STANDARD_TICKET_PRICE * seats.size()) * GROUP_DISCOUNT_RATE;
            total -= discount;
            breakdown.add(new PriceResponse.PriceBreakdownItem("Grupperabat (7% af basis)", -discount));
        }
        if (!seats.isEmpty() && seats.size() <= 5) {
            total += RESERVATION_FEE;
            breakdown.add(new PriceResponse.PriceBreakdownItem("Reservationsgebyr", RESERVATION_FEE));
        }

        return new PriceResponse(Math.round(total * 100) / 100.0, breakdown);
    }

    private ReservationResponse toResponse(Reservation reservation) {
        List<Long> seatIds = reservation.getReservedSeats().stream()
                .map(rs -> rs.getSeat().getSeatId())
                .toList();
        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getShowing().getShowingId(),
                reservation.getShowing().getMovie().getMovieId(),
                reservation.getShowing().getMovie().getTitle(),
                reservation.getCustomerName(),
                seatIds,
                reservation.getTotalPrice(),
                reservation.getBookingStatus(),
                reservation.getPaymentStatus(),
                reservation.getCreatedAt(),
                reservation.getShowing().getStartTime(),
                reservation.getShowing().getTheater().getTheaterName()
        );
    }
}
