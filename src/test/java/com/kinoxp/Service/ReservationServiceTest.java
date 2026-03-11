package com.kinoxp.Service;

import com.kinoxp.dto.ReservationRequest;
import com.kinoxp.model.movie.AgeLimit;
import com.kinoxp.model.movie.Movie;
import com.kinoxp.model.reservation.Reservation;
import com.kinoxp.model.seat.Seat;
import com.kinoxp.model.showing.Showing;
import com.kinoxp.model.user.Role;
import com.kinoxp.model.user.User;
import com.kinoxp.repository.*;
import com.kinoxp.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Movie movie = new Movie("Titanic", 195, AgeLimit.ELEVEN_PLUS);
        int numberOfTickets = 11;
        int rowNumber = 5; // standard række under 7

        double actualPrice = reservationService.calculateTotalPrice(movie, numberOfTickets, rowNumber);

        // StandardPrice 130 + langFilmFee 20 = 150
        // 11 billetter → rabat 7%
        // Total: 150 * 11 * 0.93 = 1534.5
        assertEquals(1534.5, actualPrice, 0.01);
    }


    @Test
    void calculateTotalPrice_ShouldAddLongFilmFee_WhenDurationOver170() {
        Movie movie = new Movie("Titanic", 195, AgeLimit.ELEVEN_PLUS);
        int numberOfTickets = 1;
        int rowNumber = 5;

        double actualPrice = reservationService.calculateTotalPrice(movie, numberOfTickets, rowNumber);

        // StandardPrice 130 + langFilmFee 20 = 150
        assertEquals(150.0, actualPrice, 0.01);
    }

    //TODO prisen på film der er ikke er et gebyr på.
    // Test for når der ikke er et gebyr på, altså når filmen er 170 minutter og derunder.
    @Test
    void calculateTotalPrice_ShouldAddRowFee_WhenRowNumberOver7() {
        Movie movie = new Movie("Titanic", 195, AgeLimit.ELEVEN_PLUS);
        int numberOfTickets = 1;
        int rowNumber = 8; // premium række

        double actualPrice = reservationService.calculateTotalPrice(movie, numberOfTickets, rowNumber);


        assertEquals(175.0, actualPrice, 0.01);
    }


    @Test
    void calculateTotalPrice_ShouldReturnStandardPrice_WhenShortMovieAndStandardRow() {
        Movie movie = new Movie("ShortFilm", 120, AgeLimit.ELEVEN_PLUS);
        int numberOfTickets = 1;
        int rowNumber = 5;

        double actualPrice = reservationService.calculateTotalPrice(movie, numberOfTickets, rowNumber);

        assertEquals(130.0, actualPrice, 0.01);
    }
    
    
    @Test
    void createReservation_ifRole_isEmploye(){
        //arrange

        ReservationRequest request = new ReservationRequest(1L, "Issa", List.of(4L), 10L);

        // Mock Showing and Theater relationship
        Movie movie = new Movie("Titanic", 195, AgeLimit.ELEVEN_PLUS);

        Showing showing = new Showing();
        showing.setShowingId(1L);
        showing.setMovie(movie);

        com.kinoxp.model.theater.Theater theater = new com.kinoxp.model.theater.Theater();
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

        // act:
        reservationService.createReservation(request);

        //assert:
        verify(reservationRepository).save(any(Reservation.class));




    }


}



      

