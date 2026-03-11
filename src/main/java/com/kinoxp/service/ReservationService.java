package com.kinoxp.service;

import com.kinoxp.dto.PriceResponse;
import com.kinoxp.dto.ReservationRequest;
import com.kinoxp.dto.ReservationResponse;
import com.kinoxp.model.movie.Format;
import com.kinoxp.model.movie.Movie;
import com.kinoxp.model.reservation.*;
import com.kinoxp.model.seat.Seat;
import com.kinoxp.model.showing.Showing;
import com.kinoxp.model.user.User;
import com.kinoxp.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    // US 3.2, 3.6, 3.7: Beregn totalpris inkl. standardpris, langfilm, rowFee og rabat
    private static final double STANDARD_TICKET_PRICE = 130.0;
    private static final double LONG_MOVIE_FEE = 20.0;
    private static final double THREE_D_FEE = 10.0;
    private static final double COWBOY_ROW_DISCOUNT = 30.0;
    private static final double SOFA_ROW_FEE = 25.0;
    private static final double RESERVATION_FEE = 5.0;
    private static final double GROUP_DISCOUNT_THRESHOLD = 10;
    private static final double GROUP_DISCOUNT_RATE = 0.07;

    private final ReservationRepository reservationRepository;
    private final ShowingRepository showingRepository;
    private final SeatRepository seatRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ShowingRepository showingRepository,
                              SeatRepository seatRepository,
                              ReservationSeatRepository reservationSeatRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.showingRepository = showingRepository;
        this.seatRepository = seatRepository;
        this.reservationSeatRepository = reservationSeatRepository;
        this.userRepository = userRepository;
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
    public ReservationResponse createReservation(@Valid ReservationRequest request) {
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

        double totalPrice = calculateTotalPrice(showing, seats);
        reservation.setTotalPrice(totalPrice);

        for (Seat seat : seats) {
            ReservationSeat reservationSeat = new ReservationSeat();
            reservationSeat.setSeat(seat);
            reservationSeat.setPrice(calculateSeatPrice(showing, seat));
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

    public double calculateSeatPrice(Showing showing, Seat seat) {
        double price = STANDARD_TICKET_PRICE;
        Movie movie = showing.getMovie();

        // Cowboy rækker (de to første rækker) er billigere
        if (seat.getRowNumber() <= 2) {
            price -= COWBOY_ROW_DISCOUNT;
        }
        // Sofa rækker (de bageste par rækker i de største sale) er dyrere
        // Antager at "største sale" er dem med mere end 15 rækker (baseret på DataInitializer)
        else if (showing.getTheater().getTotalRows() > 15 &&
                seat.getRowNumber() > showing.getTheater().getTotalRows() - 2) {
            price += SOFA_ROW_FEE;
        }

        // Helaftensfilm (> 170 min) tillæg
        if (movie.getDurationInMinutes() > 170) {
            price += LONG_MOVIE_FEE;
        }

        // 3D film tillæg
        if (movie.getFormat() == Format.FORMAT_3D) {
            price += THREE_D_FEE;
        }

        return price;
    }

    public double calculateTotalPrice(Showing showing, List<Seat> seats) {
        double totalTicketPrice = 0;
        for (Seat seat : seats) {
            totalTicketPrice += calculateSeatPrice(showing, seat);
        }

        // Gruppe-rabat (pt. 7% af billetprisen uden tillæg) for mere end 10 billetter
        // "Uden tillæg" tolkes som 7% af (STANDARD_TICKET_PRICE * antal billetter)
        if (seats.size() > GROUP_DISCOUNT_THRESHOLD) {
            double discount = (STANDARD_TICKET_PRICE * seats.size()) * GROUP_DISCOUNT_RATE;
            totalTicketPrice -= discount;
        }

        // Reservationsgebyr på 5 eller færre billetter
        if (seats.size() <= 5) {
            totalTicketPrice += RESERVATION_FEE;
        }

        return Math.round(totalTicketPrice * 100) / 100.0;
    }

    public PriceResponse calculatePriceFromRequest(PriceRequest request) {
        Showing showing = showingRepository.findById(request.getShowingId())
                .orElseThrow(() -> new RuntimeException("Showing not found"));

        List<Seat> seats = seatRepository.findAllById(request.getSeatIds());

        return calculateFullPriceDetails(showing, seats);
    }

    public PriceResponse calculateFullPriceDetails(Showing showing, List<Seat> seats) {
        double totalTicketPrice = 0;
        List<PriceResponse.PriceBreakdownItem> breakdown = new ArrayList<>();

        int cowboySeats = 0;
        int sofaSeats = 0;
        int standardSeats = 0;

        Movie movie = showing.getMovie();
        boolean isLongFilm = movie.getDurationInMinutes() > 170;
        boolean is3D = movie.getFormat() == Format.FORMAT_3D;

        for (Seat seat : seats) {
            double seatBasePrice = STANDARD_TICKET_PRICE;
            if (seat.getRowNumber() <= 2) {
                seatBasePrice -= COWBOY_ROW_DISCOUNT;
                cowboySeats++;
            } else if (showing.getTheater().getTotalRows() > 15 &&
                    seat.getRowNumber() > showing.getTheater().getTotalRows() - 2) {
                seatBasePrice += SOFA_ROW_FEE;
                sofaSeats++;
            } else {
                standardSeats++;
            }

            if (isLongFilm) seatBasePrice += LONG_MOVIE_FEE;
            if (is3D) seatBasePrice += THREE_D_FEE;

            totalTicketPrice += seatBasePrice;
        }

        if (standardSeats > 0) breakdown.add(new PriceResponse.PriceBreakdownItem("Standard billetter (" + standardSeats + " stk)", standardSeats * STANDARD_TICKET_PRICE));
        if (cowboySeats > 0) breakdown.add(new PriceResponse.PriceBreakdownItem("Cowboy-rækker (" + cowboySeats + " stk)", cowboySeats * (STANDARD_TICKET_PRICE - COWBOY_ROW_DISCOUNT)));
        if (sofaSeats > 0) breakdown.add(new PriceResponse.PriceBreakdownItem("Sofa-rækker (" + sofaSeats + " stk)", sofaSeats * (STANDARD_TICKET_PRICE + SOFA_ROW_FEE)));

        if (isLongFilm) breakdown.add(new PriceResponse.PriceBreakdownItem("Tillæg: Helaftensfilm", seats.size() * LONG_MOVIE_FEE));
        if (is3D) breakdown.add(new PriceResponse.PriceBreakdownItem("Tillæg: 3D-film", seats.size() * THREE_D_FEE));

        if (seats.size() > GROUP_DISCOUNT_THRESHOLD) {
            double discount = (STANDARD_TICKET_PRICE * seats.size()) * GROUP_DISCOUNT_RATE;
            totalTicketPrice -= discount;
            breakdown.add(new PriceResponse.PriceBreakdownItem("Grupperabat (7% af basis)", -discount));
        }

        if (seats.size() > 0 && seats.size() <= 5) {
            totalTicketPrice += RESERVATION_FEE;
            breakdown.add(new PriceResponse.PriceBreakdownItem("Reservationsgebyr", RESERVATION_FEE));
        }

        double finalPrice = Math.round(totalTicketPrice * 100) / 100.0;
        return new PriceResponse(finalPrice, breakdown);
    }

    public List<ReservationResponse> getReservationsByCustomerName(String customerName) {
        return reservationRepository.findByCustomerName(customerName)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<Long> getReservedSeatIdsByShowingId(Long showingId) {
        return reservationSeatRepository.findByReservation_Showing_ShowingId(showingId)
                .stream()
                .map(rs -> rs.getSeat().getSeatId())
                .toList();
    }
}
