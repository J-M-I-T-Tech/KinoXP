package com.kinoxp.Service;

import com.kinoxp.model.movie.AgeLimit;
import com.kinoxp.model.movie.Movie;
import com.kinoxp.model.seat.Seat;
import com.kinoxp.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationServiceTest {

    private ReservationService reservationService;

@BeforeEach
    void setUp(){
    reservationService = new ReservationService();
}

@Test
    void calculatePrice_AddFee_WhenMovieIsOver170Minutes(){
    //Arrange
    Movie langFilm = new Movie("Titanic", 195, AgeLimit.ELEVEN_PLUS);
    double standardPrice = 130.0;
    double langFilmFee = 20.0;

    //act
    //US 3.6: en film der over 170 minutter.
    double actualPrice = reservationService.calculateMoviePrice(langFilm,standardPrice,langFilmFee);

    //Assert
    assertEquals(150.0, actualPrice, "der betales et gebyr på film over 170 minutter");
}
//TODO prisen på film der er ikke er et gebyr på.
       // Test for når der ikke er et gebyr på, altså når filmen er 170 minutter og derunder.
    @Test
    void calculatePrice_NormalPrice_WithoutFee_WhenMovieIsExactly170Minutter(){

    //Arrange
        Movie limitMovie = new Movie("præcis 170", 170 , AgeLimit.FIFTEEN_PLUS);
        double standardPrice = 130.0;
        double langFilmFee = 20.0;


        //Act
        //Film på præcis 170 min eller derunder skal afregnes til normalpris
        double acutalPrice = reservationService.calculateMoviePrice(limitMovie,standardPrice,langFilmFee);

            //Assert
            assertEquals(130,acutalPrice, "film der præcis er 170 minutter eller der under, har ingen gebyr" );
        }

        // US: 3.7: Som kunde vil jeg opleve at prisen justeres afhængigt af sæderækker.

    @Test
    void calculatePrice_AddRowFee_whenItIsPremium(){

    //Arrange
        Movie movie = new Movie("Titanic", 195, AgeLimit.ELEVEN_PLUS);
        Seat premiumSeat = new Seat(12,8);
        double standardPrice = 130.0;
        double rowFee = 25.0;

        //Act
        double actualPrice = reservationService.calculateSeatPrice(premiumSeat,standardPrice,rowFee,movie);

        //Assert
        assertEquals(155.0, actualPrice, "Prisen skal stige med 25 kr., når sædet er på en premium række");

    }


        }
      

