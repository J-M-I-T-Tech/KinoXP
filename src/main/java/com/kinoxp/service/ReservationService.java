package com.kinoxp.service;
import com.kinoxp.dto.ReservationRequest;
import com.kinoxp.dto.ReservationResponse;
import com.kinoxp.model.movie.Movie;
import com.kinoxp.model.reservation.*;
import com.kinoxp.model.seat.Seat;
import com.kinoxp.model.showing.Showing;
import com.kinoxp.model.user.Role;
import com.kinoxp.model.user.User;
import com.kinoxp.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import com.kinoxp.model.reservation.PriceRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    // US 3.2, 3.6, 3.7: Beregn totalpris inkl. standardpris, langfilm, rowFee og rabat
    private static final double STANDARD_PRICE = 130.0;
    private static final double LONG_FILM_FEE = 20.0;
    private static final double ROW_FEE = 25.0;
    private static final double RABAT_HVIS_MERE_END_10 = 0.07;

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
                .orElseThrow( () -> new RuntimeException("Acces denied"));
        if(user.getRole() !=Role.EMPLOYEE) {
            throw new RuntimeException("Acces denied");
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
        // denne metode glemmer at tage gebyrene med
//        reservation.setTotalPrice(STANDARD_PRICE * seats.size());

        // her får vi os gebyrene med både på longfilmfee og rowfee
        int rowNumber = seats.stream()
                .mapToInt(Seat::getRowNumber)
                .max()
                .orElse(1);
        double totalPrice = calculateTotalPrice(showing.getMovie(), seats.size(), rowNumber);
        reservation.setTotalPrice(totalPrice);

        for (Seat seat : seats) {
            ReservationSeat reservationSeat = new ReservationSeat();
            reservationSeat.setSeat(seat);
            reservationSeat.setPrice(STANDARD_PRICE);
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

    // Beregn pris ud fra Movie, antal billetter og række
    public double calculateTotalPrice(Movie movie, int numberOfTickets, int rowNumber) {
        double pricePerTicket = STANDARD_PRICE;

        // Langfilm-gebyr (US 3.6)
        if (movie.getDurationInMinutes() > 170) {
            pricePerTicket += LONG_FILM_FEE;
        }

        // Row fee for premium rækker (US 3.7)
        if (rowNumber > 7) {
            pricePerTicket += ROW_FEE;
        }

        // Totalpris uden rabat
        double totalPrice = pricePerTicket * numberOfTickets;

        // Mængderabat (US 3.2)
        if (numberOfTickets > 10) {
            totalPrice *= (1 - RABAT_HVIS_MERE_END_10);
        }

        //afrunder til 2 decimaler efter komma
        return Math.round(totalPrice * 100) / 100.0;
    }

    public double calculatePriceFromRequest(PriceRequest request) {
        // Hent Showing og Movie
        Showing showing = showingRepository.findById(request.getShowingId())
                .orElseThrow(() -> new RuntimeException("Showing not found"));

        Movie movie = showing.getMovie();

        // Beregn totalpris inkl. langfilmgebyr, rækkegebyr og rabat
        return calculateTotalPrice(
                movie,
                request.getNumberOfTickets(),
                request.getRowNumber()
        );
    }

    public List<ReservationResponse> getReservationsByCustomerName(String customerName) {
        return reservationRepository.findByCustomerName(customerName)
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
