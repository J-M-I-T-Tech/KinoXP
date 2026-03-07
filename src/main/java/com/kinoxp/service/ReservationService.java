package com.kinoxp.service;

import com.kinoxp.model.movie.Movie;
import com.kinoxp.model.seat.Seat;
import com.kinoxp.model.theater.Theater;

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

    //US:3.7
    public double calculateSeatPrice(Seat seat, double standardPrice, double rowFee, Movie movie) {
    // logik for hvis man køber en sæde efter række 7, er der gebyr på
        if (seat.getRowNumber() > 7){
            return standardPrice +  rowFee;
        }
        return standardPrice;
    }

    //US:3.2 Som kunde vil jeg have mængderabat, hvis jeg reserverer mere end 10 billetter.
    public double calculateWithDiscount(Movie movie, double standardPrice, double langFilmFee, double rowFee, double discount,
                                        int numberOfTickets) {
    //Pris pr billet
    double pricePerTicket = standardPrice + rowFee;

    if (movie.getDurationInMinutes() > 170){
        pricePerTicket +=  langFilmFee;
    }
  // total pris før rabat
    double totalPrice = pricePerTicket * numberOfTickets;

        if (numberOfTickets > 10) {
            totalPrice *= (1 - discount);
        }

        return totalPrice;
    }

}
