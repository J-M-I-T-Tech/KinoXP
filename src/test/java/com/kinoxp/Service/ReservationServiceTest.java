package com.kinoxp.Service;

import com.kinoxp.model.movie.AgeLimit;
import com.kinoxp.model.movie.Movie;
import com.kinoxp.model.seat.Seat;
import com.kinoxp.model.showing.Showing;
import com.kinoxp.repository.*;
import com.kinoxp.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(reservationRepository, showingRepository, seatRepository, reservationSeatRepository);
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

}

      

