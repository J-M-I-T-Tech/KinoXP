package com.kinoxp.service;

import com.kinoxp.dto.PriceResponse;
import com.kinoxp.dto.ReservationRequest;
import com.kinoxp.model.movie.AgeLimit;
import com.kinoxp.model.movie.Format;
import com.kinoxp.model.movie.Movie;
import com.kinoxp.model.reservation.Reservation;
import com.kinoxp.model.seat.Seat;
import com.kinoxp.model.showing.Showing;
import com.kinoxp.model.theater.Theater;
import com.kinoxp.model.user.Role;
import com.kinoxp.model.user.User;
import com.kinoxp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ShowingRepository showingRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private ReservationSeatRepository reservationSeatRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(reservationRepository, showingRepository, seatRepository, reservationSeatRepository, userRepository);
    }

    @Test
    void calculateTotalPrice_ShouldGive7ProcentDiscount_WhenMoreThan10Tickets() {
        Movie movie = new Movie("Titanic", 195, AgeLimit.ELLEVE_PLUS);
        movie.setFormat(Format.FORMAT_2D);
        Theater theater = new Theater(1L, "Sal 1", 20, 10);
        Showing showing = new Showing();
        showing.setMovie(movie);
        showing.setTheater(theater);

        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            Seat seat = new Seat(i, 5); // række 5
            seat.setTheater(theater);
            seats.add(seat);
        }

        double actualPrice = reservationService.calculateTotalPrice(showing, seats);

        // SeatPrice: Standard 130 + langFilmFee 20 = 150
        // TotalTicketPrice: 150 * 11 = 1650
        // Rabat: 7% af (130 * 11) = 0.07 * 1430 = 100.1
        // Forventet: 1650 - 100.1 = 1549.9
        assertEquals(1549.9, actualPrice, 0.01);
    }

    @Test
    void calculateTotalPrice_ShouldAddLongFilmFee_WhenDurationOver170() {
        Movie movie = new Movie("Titanic", 195, AgeLimit.ELLEVE_PLUS);
        movie.setFormat(Format.FORMAT_2D);
        Theater theater = new Theater(1L, "Sal 1", 10, 10);
        Showing showing = new Showing();
        showing.setMovie(movie);
        showing.setTheater(theater);

        Seat seat = new Seat(1, 5);
        seat.setTheater(theater);

        double actualPrice = reservationService.calculateTotalPrice(showing, List.of(seat));

        // StandardPrice 130 + langFilmFee 20 + reservationFee 5 = 155
        assertEquals(155.0, actualPrice, 0.01);
    }

    @Test
    void calculateTotalPrice_ShouldAddSofaFee_WhenInLargeTheaterAndLastRows() {
        Movie movie = new Movie("Titanic", 195, AgeLimit.ELLEVE_PLUS);
        movie.setFormat(Format.FORMAT_2D);
        // Stor sal > 15 rækker
        Theater theater = new Theater(1L, "Sal 1", 20, 10);
        Showing showing = new Showing();
        showing.setMovie(movie);
        showing.setTheater(theater);

        Seat seat = new Seat(1, 20); // Sidste række
        seat.setTheater(theater);

        double actualPrice = reservationService.calculateTotalPrice(showing, List.of(seat));

        // Standard 130 + langfilm 20 + sofaFee 25 + resFee 5 = 180
        assertEquals(180.0, actualPrice, 0.01);
    }

    @Test
    void calculateTotalPrice_ShouldApplyCowboyDiscount_WhenFirstTwoRows() {
        Movie movie = new Movie("Short", 120, AgeLimit.ELLEVE_PLUS);
        movie.setFormat(Format.FORMAT_2D);
        Theater theater = new Theater(1L, "Sal 1", 10, 10);
        Showing showing = new Showing();
        showing.setMovie(movie);
        showing.setTheater(theater);

        Seat seat = new Seat(1, 1); // Første række
        seat.setTheater(theater);

        double actualPrice = reservationService.calculateTotalPrice(showing, List.of(seat));

        // Standard 130 - cowboyDiscount 15 + resFee 5 = 120
        assertEquals(120.0, actualPrice, 0.01);
    }

    @Test
    void calculateTotalPrice_ShouldAdd3DFee_WhenMovieIs3D() {
        Movie movie = new Movie("Avatar", 120, AgeLimit.ELLEVE_PLUS);
        movie.setFormat(Format.FORMAT_3D);
        Theater theater = new Theater(1L, "Sal 1", 10, 10);
        Showing showing = new Showing();
        showing.setMovie(movie);
        showing.setTheater(theater);

        Seat seat = new Seat(1, 5);
        seat.setTheater(theater);

        double actualPrice = reservationService.calculateTotalPrice(showing, List.of(seat));

        // Standard 130 + 3D fee 30 + resFee 5 = 165
        assertEquals(165.0, actualPrice, 0.01);
    }

    @Test
    void createReservation_ifRole_isEmploye() {
        // Arrange
        ReservationRequest request = new ReservationRequest(1L, "Issa", List.of(4L), 10L);

        // Mock Showing and Theater relationship
        Movie movie = new Movie("Titanic", 195, AgeLimit.ELLEVE_PLUS);

        Showing showing = new Showing();
        showing.setShowingId(1L);
        showing.setMovie(movie);

        Theater theater = new Theater();
        theater.setTheaterId(100L);
        showing.setTheater(theater);

        // Mock Seat belonging to the same theater
        Seat seat = new Seat();
        seat.setSeatId(4L);
        seat.setRowNumber(5);
        seat.setSeatNumber(10);
        seat.setTheater(theater);

        User user = new User();
        user.setRole(Role.EMPLOYEE);
        user.setUserId(10L);

        when(showingRepository.findById(1L)).thenReturn(Optional.of(showing));
        when(seatRepository.findAllById(List.of(4L))).thenReturn(List.of(seat));
        when(reservationSeatRepository
                .existsByReservation_Showing_ShowingIdAndSeat_SeatId(1L, 4L)).thenReturn(false);
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        reservationService.createReservation(request);

        // Assert
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void calculateFullPriceDetails_ShouldReturnBreakdown() {
        Movie movie = new Movie("Avatar 3D", 195, AgeLimit.ELLEVE_PLUS);
        movie.setFormat(Format.FORMAT_3D);
        Theater theater = new Theater(1L, "Sal 1", 10, 10);
        Showing showing = new Showing();
        showing.setMovie(movie);
        showing.setTheater(theater);

        Seat cowboySeat = new Seat(1, 1);
        cowboySeat.setTheater(theater);
        Seat standardSeat = new Seat(2, 5);
        standardSeat.setTheater(theater);

        PriceResponse response = reservationService.calculateFullPriceDetails(showing, List.of(cowboySeat, standardSeat));

        // Cowboy: (130 - 15) = 115
        // Standard: 130
        // Long movie: 2 * 20 = 40
        // 3D: 2 * 30 = 60
        // Res fee: 5 (for 2 tickets)
        // Total: 115 + 130 + 40 + 60 + 5 = 350
        assertEquals(350.0, response.totalPrice(), 0.01);
        assertEquals(5, response.breakdown().size());

        assertTrue(response.breakdown().stream().anyMatch(i -> i.label().contains("Standard billetter")));
        assertTrue(response.breakdown().stream().anyMatch(i -> i.label().contains("Cowboy-rækker")));
        assertTrue(response.breakdown().stream().anyMatch(i -> i.label().contains("Helaftensfilm")));
        assertTrue(response.breakdown().stream().anyMatch(i -> i.label().contains("3D-film")));
        assertTrue(response.breakdown().stream().anyMatch(i -> i.label().contains("Reservationsgebyr")));
    }
}
