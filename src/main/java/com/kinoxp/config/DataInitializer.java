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
import com.kinoxp.model.user.Role;
import com.kinoxp.model.user.User;
import com.kinoxp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
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
                                      ReservationRepository reservationRepository, UserRepository userRepository) {
        return args -> {
            // Vi kører kun initialiseringen, hvis databasen er tom
            if (movieRepository.count() == 0) {

                // Movies
                Movie movie1 = movieRepository.save(new Movie(null, "Inception", 2010, Genre.SCIENCE_FICTION, 148, Format.FORMAT_2D, AgeLimit.ELVE_PLUS, Language.ENGELSK));
                Movie movie2 = movieRepository.save(new Movie(null, "The Notebook", 2004, Genre.ROMANTISK, 123, Format.FORMAT_2D, AgeLimit.SYV_PLUS, Language.ENGELSK));
                Movie movie3 = movieRepository.save(new Movie(null, "Get Out", 2017, Genre.GYSER, 104, Format.FORMAT_2D, AgeLimit.FEMTEN_PLUS, Language.ENGELSK));
                Movie movie4 = movieRepository.save(new Movie(null, "Mad Max: Fury Road", 2015, Genre.ACTION, 120, Format.FORMAT_3D, AgeLimit.FEMTEN_PLUS, Language.ENGELSK));
                Movie movie5 = movieRepository.save(new Movie(null, "Badehotellet: Special", 2022, Genre.ROMANTISK, 95, Format.FORMAT_2D, AgeLimit.Alle, Language.DANSK));
                Movie movie6 = movieRepository.save(new Movie(null, "Planet X", 2024, Genre.SCIENCE_FICTION, 132, Format.FORMAT_3D, AgeLimit.ELVE_PLUS, Language.ENGELSK));
                Movie movie7 = movieRepository.save(new Movie(null, "Night Train", 2021, Genre.ACTION, 110, Format.FORMAT_2D, AgeLimit.SYV_PLUS, Language.DANSK));
                Movie movie8 = movieRepository.save(new Movie(null, "Haunted Hall", 2019, Genre.GYSER, 99, Format.FORMAT_2D, AgeLimit.FEMTEN_PLUS, Language.ENGELSK));
                Movie movie9 = movieRepository.save(new Movie(null, "Titanic", 1997, Genre.ROMANTISK, 195, Format.FORMAT_2D, AgeLimit.ELVE_PLUS, Language.ENGELSK));
                Movie movie10 = movieRepository.save(new Movie(null, "Druk", 2020, Genre.ACTION, 117, Format.FORMAT_2D, AgeLimit.ELVE_PLUS, Language.DANSK));

                // Theaters
                Theater theater1 = theaterRepository.save(new Theater(null, "Small Theater", 20, 12));
                Theater theater2 = theaterRepository.save(new Theater(null, "Large Theater", 25, 16));

                // Seats
                createSeatsForTheater(theater1, seatRepository);
                createSeatsForTheater(theater2, seatRepository);

                // Showings
                Showing showing1 = showingRepository.save(new Showing(null, movie1, theater1, LocalDateTime.now().plusDays(5).withHour(19).withMinute(0), LocalDateTime.now().plusDays(5).withHour(21).withMinute(28), ShowingStatus.UPCOMING));
                Showing showing2 = showingRepository.save(new Showing(null, movie3, theater1, LocalDateTime.now().plusDays(5).withHour(22).withMinute(0), LocalDateTime.now().plusDays(5).withHour(23).withMinute(44), ShowingStatus.UPCOMING));
                Showing showing3 = showingRepository.save(new Showing(null, movie9, theater2, LocalDateTime.now().plusDays(6).withHour(18).withMinute(30), LocalDateTime.now().plusDays(6).withHour(21).withMinute(45), ShowingStatus.UPCOMING));
                showingRepository.save(new Showing(null, movie10, theater1, LocalDateTime.now().plusDays(6).withHour(21).withMinute(0), LocalDateTime.now().plusDays(6).withHour(22).withMinute(57), ShowingStatus.CANCELLED));
                showingRepository.save(new Showing(null, movie10, theater2, LocalDateTime.now().minusDays(1).withHour(21).withMinute(0), LocalDateTime.now().minusDays(1).withHour(22).withMinute(57), ShowingStatus.COMPLETED));

                // Opret Reservationer (Placeholders)
                // Hent sæder for at kunne knytte dem til reservationer
                List<Seat> theater1Seats = seatRepository.findAll().stream().filter(s -> s.getTheater().getTheaterId().equals(theater1.getTheaterId())).toList();
                List<Seat> theater2Seats = seatRepository.findAll().stream().filter(s -> s.getTheater().getTheaterId().equals(theater2.getTheaterId())).toList();

                User user1 = userRepository.save(new User(null, "Alice Jensen", LocalDate.of(1990, 5, 15), Role.CUSTOMER, "password123"));
                User user2 = userRepository.save(new User(null, "Mikkel Hansen", LocalDate.of(1999, 10, 13), Role.EMPLOYEE, "password123"));
                User user3 = userRepository.save(new User(null, "Sara Nielsen", LocalDate.of(1921, 1, 1), Role.ADMIN, "password123"));

                // Reservation 1: Alice Jensen (2 sæder)
                Reservation reservation1 = new Reservation(null, showing1, user1, LocalDateTime.now(), 200.0, BookingStatus.CONFIRMED, PaymentStatus.PAID);
                reservation1.addReservedSeat(new ReservationSeat(null, reservation1, theater1Seats.get(0), 100.0)); // Sæde 1, Række 1
                reservation1.addReservedSeat(new ReservationSeat(null, reservation1, theater1Seats.get(1), 100.0)); // Sæde 2, Række 1
                reservationRepository.save(reservation1);

                // Reservation 2: Mikkel Hansen (1 sæde)
                Reservation reservation2 = new Reservation(null, showing2, user2, LocalDateTime.now(), 100.0, BookingStatus.CONFIRMED, PaymentStatus.AWAITING);
                reservation2.addReservedSeat(new ReservationSeat(null, reservation2, theater1Seats.get(2), 100.0)); // Sæde 3, Række 1
                reservationRepository.save(reservation2);

                // Reservation 3: Sara Nielsen (3 sæder i den store sal)
                Reservation reservation3 = new Reservation(null, showing3, user3, LocalDateTime.now(), 300.0, BookingStatus.PENDING, PaymentStatus.AWAITING);
                reservation3.addReservedSeat(new ReservationSeat(null, reservation3, theater2Seats.get(0), 100.0));
                reservation3.addReservedSeat(new ReservationSeat(null, reservation3, theater2Seats.get(1), 100.0));
                reservation3.addReservedSeat(new ReservationSeat(null, reservation3, theater2Seats.get(2), 100.0));
                reservationRepository.save(reservation3);
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
