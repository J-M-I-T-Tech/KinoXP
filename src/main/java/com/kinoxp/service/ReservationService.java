package com.kinoxp.service;

import com.kinoxp.model.movie.Movie;

public class ReservationService {

    //US: 3.6
    public double calculateMoviePrice(Movie movie, double standardPrice, double langFilmFee) {
    //logik: hvis en film er over 170 min. kommer der film gebyr på.
        if(movie.getDurationInMinutes() > 170){
            return standardPrice +  langFilmFee;
        }
        // her skal returnerer den normalprisen.
    return standardPrice;

    }

}
