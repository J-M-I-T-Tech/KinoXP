package com.kinoxp.config;

import com.kinoxp.model.movie.*;
import com.kinoxp.model.reservation.BookingStatus;
import com.kinoxp.model.reservation.PaymentStatus;
import com.kinoxp.model.reservation.Reservation;
import com.kinoxp.model.reservation.ReservationSeat;
import com.kinoxp.model.seat.Seat;
import com.kinoxp.model.showing.Showing;
import com.kinoxp.model.showing.ShowingStatus;
import com.kinoxp.model.theater.Theater;
import com.kinoxp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(TheaterRepository theaterRepository,
                                      SeatRepository seatRepository,
                                      MovieRepository movieRepository,
                                      ShowingRepository showingRepository,
                                      ReservationRepository reservationRepository) {
        return args -> {
            // Vi kører kun initialiseringen, hvis databasen er tom
            if (movieRepository.count() == 0) {

                // Movies
                Movie m1 = movieRepository.save(new Movie(null, "Inception", 2010, Genre.SCIENCE_FICTION, 148, Format.TWO_DIMENSIONAL, AgeLimit.ELEVEN_PLUS, Language.ENGLISH));
                Movie m2 = movieRepository.save(new Movie(null, "The Notebook", 2004, Genre.ROMANCE, 123, Format.TWO_DIMENSIONAL, AgeLimit.SEVEN_PLUS, Language.ENGLISH));
                Movie m3 = movieRepository.save(new Movie(null, "Get Out", 2017, Genre.HORROR, 104, Format.TWO_DIMENSIONAL, AgeLimit.FIFTEEN_PLUS, Language.ENGLISH));
                Movie m4 = movieRepository.save(new Movie(null, "Mad Max: Fury Road", 2015, Genre.ACTION, 120, Format.THREE_DIMENSIONAL, AgeLimit.FIFTEEN_PLUS, Language.ENGLISH));
                Movie m5 = movieRepository.save(new Movie(null, "Badehotellet: Special", 2022, Genre.ROMANCE, 95, Format.TWO_DIMENSIONAL, AgeLimit.ALL, Language.DANISH));
                Movie m6 = movieRepository.save(new Movie(null, "Planet X", 2024, Genre.SCIENCE_FICTION, 132, Format.THREE_DIMENSIONAL, AgeLimit.ELEVEN_PLUS, Language.ENGLISH));
                Movie m7 = movieRepository.save(new Movie(null, "Night Train", 2021, Genre.ACTION, 110, Format.TWO_DIMENSIONAL, AgeLimit.SEVEN_PLUS, Language.DANISH));
                Movie m8 = movieRepository.save(new Movie(null, "Haunted Hall", 2019, Genre.HORROR, 99, Format.TWO_DIMENSIONAL, AgeLimit.FIFTEEN_PLUS, Language.ENGLISH));
                Movie m9 = movieRepository.save(new Movie(null, "Titanic", 1997, Genre.ROMANCE, 195, Format.TWO_DIMENSIONAL, AgeLimit.ELEVEN_PLUS, Language.ENGLISH));
                Movie m10 = movieRepository.save(new Movie(null, "Druk", 2020, Genre.ACTION, 117, Format.TWO_DIMENSIONAL, AgeLimit.ELEVEN_PLUS, Language.DANISH));

                // Theaters
                Theater t1 = theaterRepository.save(new Theater(null, "Small Theater", 20, 12));
                Theater t2 = theaterRepository.save(new Theater(null, "Large Theater", 25, 16));

                // Seats
                createSeatsForTheater(t1, seatRepository);
                createSeatsForTheater(t2, seatRepository);

                // Showings
                Showing s1 = showingRepository.save(new Showing(null, m1, t1, LocalDateTime.now().plusDays(5).withHour(19).withMinute(0), LocalDateTime.now().plusDays(5).withHour(21).withMinute(28), ShowingStatus.UPCOMING));
                Showing s2 = showingRepository.save(new Showing(null, m3, t1, LocalDateTime.now().plusDays(5).withHour(22).withMinute(0), LocalDateTime.now().plusDays(5).withHour(23).withMinute(44), ShowingStatus.UPCOMING));
                Showing s3 = showingRepository.save(new Showing(null, m9, t2, LocalDateTime.now().plusDays(6).withHour(18).withMinute(30), LocalDateTime.now().plusDays(6).withHour(21).withMinute(45), ShowingStatus.UPCOMING));
                showingRepository.save(new Showing(null, m10, t1, LocalDateTime.now().plusDays(6).withHour(21).withMinute(0), LocalDateTime.now().plusDays(6).withHour(22).withMinute(57), ShowingStatus.CANCELLED));
                showingRepository.save(new Showing(null, m10, t2, LocalDateTime.now().minusDays(1).withHour(21).withMinute(0), LocalDateTime.now().minusDays(1).withHour(22).withMinute(57), ShowingStatus.COMPLETED));

                // Opret Reservationer (Placeholders)
                // Hent sæder for at kunne knytte dem til reservationer
                List<Seat> t1Seats = seatRepository.findAll().stream().filter(s -> s.getTheater().getTheaterId().equals(t1.getTheaterId())).toList();
                List<Seat> t2Seats = seatRepository.findAll().stream().filter(s -> s.getTheater().getTheaterId().equals(t2.getTheaterId())).toList();

                // Reservation 1: Alice Jensen (2 sæder)
                Reservation r1 = new Reservation(null, s1, "Alice Jensen", LocalDateTime.now(), 200.0, BookingStatus.CONFIRMED, PaymentStatus.PAID);
                r1.addReservedSeat(new ReservationSeat(null, r1, t1Seats.get(0), 100.0)); // Sæde 1, Række 1
                r1.addReservedSeat(new ReservationSeat(null, r1, t1Seats.get(1), 100.0)); // Sæde 2, Række 1
                reservationRepository.save(r1);

                // Reservation 2: Mikkel Hansen (1 sæde)
                Reservation r2 = new Reservation(null, s1, "Mikkel Hansen", LocalDateTime.now(), 100.0, BookingStatus.CONFIRMED, PaymentStatus.AWAITING);
                r2.addReservedSeat(new ReservationSeat(null, r2, t1Seats.get(2), 100.0)); // Sæde 3, Række 1
                reservationRepository.save(r2);

                // Reservation 3: Sara Nielsen (3 sæder i den store sal)
                Reservation r3 = new Reservation(null, s3, "Sara Nielsen", LocalDateTime.now(), 300.0, BookingStatus.PENDING, PaymentStatus.AWAITING);
                r3.addReservedSeat(new ReservationSeat(null, r3, t2Seats.get(0), 100.0));
                r3.addReservedSeat(new ReservationSeat(null, r3, t2Seats.get(1), 100.0));
                r3.addReservedSeat(new ReservationSeat(null, r3, t2Seats.get(2), 100.0));
                reservationRepository.save(r3);
            }
        };
    }

    private void createSeatsForTheater(Theater theater, SeatRepository seatRepository) {
        List<Seat> seats = new ArrayList<>();
        for (int row = 1; row <= theater.getTotalRows(); row++) {
            for (int seatNum = 1; seatNum <= theater.getSeatsPerRow(); seatNum++) {
                Seat seat = new Seat();
                seat.setTheater(theater);
                seat.setRowNumber(row);
                seat.setSeatNumber(seatNum);
                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
    }
}
