package com.kinoxp.Service;

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
    Movie langFilm = new Movie("Titanic", 195, "12+");
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
        Movie limitMovie = new Movie("præcis 170", 170 , "12+");
        double standardPrice = 130.0;
        double langFilmFee = 20.0;


        //Act
        //Film på præcis 170 min eller derunder skal afregnes til normalpris
        double acutalPrice = reservationService.calculateMoviePrice(limitMovie,standardPrice,langFilmFee);

            //Arrange
            assertEquals(130,acutalPrice, "film der præcis er 170 minutter eller der under, har ingen gebyr" );
        }
        }
      

