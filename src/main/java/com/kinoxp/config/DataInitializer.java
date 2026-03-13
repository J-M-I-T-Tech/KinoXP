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
import java.util.Random;

@Configuration
public class DataInitializer {
    //momo

    @Bean
    public CommandLineRunner initData(TheaterRepository theaterRepository,
                                      SeatRepository seatRepository,
                                      MovieRepository movieRepository,
                                      ShowingRepository showingRepository,
                                      ReservationRepository reservationRepository,
                                      UserRepository userRepository,
                                      ReservationSeatRepository reservationSeatRepository) {
        return args -> {
            // Vi kører kun initialiseringen, hvis databasen er tom
            if (movieRepository.count() == 0) {

                // Movies
                Movie movie1 = movieRepository.save(new Movie(null, "Inception",
                        "En dygtig tyv specialiserer sig i at stjæle hemmeligheder fra folks drømme, men får en farlig opgave: at plante en idé i en persons underbevidsthed.",
                        2010, Genre.SCIENCE_FICTION, 148, Format.FORMAT_3D, AgeLimit.ELLEVE_PLUS, Language.ENGELSK));

                Movie movie2 = movieRepository.save(new Movie(null, "The Notebook",
                        "En ældre mand læser historien om to unge elskende for en kvinde med demens, hvis romance er truet af forskellen i deres sociale klasser.",
                        2004, Genre.ROMANTISK, 123, Format.FORMAT_2D, AgeLimit.SYV_PLUS, Language.ENGELSK));

                Movie movie3 = movieRepository.save(new Movie(null, "Get Out",
                        "En ung mand besøger sin kærestes familie, men opdager hurtigt at noget mørkt og uhyggeligt gemmer sig bag deres venlige facade.",
                        2017, Genre.GYSER, 104, Format.FORMAT_2D, AgeLimit.FEMTEN_PLUS, Language.ENGELSK));

                Movie movie4 = movieRepository.save(new Movie(null, "Mad Max: Fury Road",
                        "I en postapokalyptisk ørken slår Max sig sammen med Furiosa for at flygte fra en tyrannisk krigsherre i et hæsblæsende biljagt-eventyr.",
                        2015, Genre.ACTION, 120, Format.FORMAT_2D, AgeLimit.FEMTEN_PLUS, Language.ENGELSK));

                Movie movie5 = movieRepository.save(new Movie(null, "Interstellar",
                        "I en fremtid hvor Jorden er ved at blive ubeboelig, rejser et hold astronauter gennem et ormehul i søgen efter et nyt hjem til menneskeheden.",
                        2014, Genre.SCIENCE_FICTION, 169, Format.FORMAT_2D, AgeLimit.ELLEVE_PLUS, Language.ENGELSK));

                Movie movie6 = movieRepository.save(new Movie(null, "The Godfather",
                        "Den aldrende overhoved for en kriminel dynasti overdrager kontrollen med sit imperium til sin modvillige søn.",
                        1972, Genre.ACTION, 175, Format.FORMAT_2D, AgeLimit.FEMTEN_PLUS, Language.ENGELSK));

                Movie movie7 = movieRepository.save(new Movie(null, "The Dark Knight",
                        "Da truslen kendt som Jokeren skaber kaos i Gotham, må Batman acceptere en af de største psykologiske og fysiske prøvelser i sin kamp mod uretfærdighed.",
                        2008, Genre.ACTION, 152, Format.FORMAT_2D, AgeLimit.ELLEVE_PLUS, Language.ENGELSK));

                Movie movie8 = movieRepository.save(new Movie(null, "Pulp Fiction",
                        "Livet for to lejemordere, en bokser, en gangsters kone og et par småforbrydere flettes sammen i fire fortællinger om vold og forløsning.",
                        1994, Genre.ACTION, 154, Format.FORMAT_2D, AgeLimit.FEMTEN_PLUS, Language.ENGELSK));

                Movie movie9 = movieRepository.save(new Movie(null, "Titanic",
                        "En fattig kunstner og en rig kvinde forelsker sig ombord på det luksuriøse skib Titanic, men deres kærlighed sættes på prøve da skibet rammer et isbjerg.",
                        1997, Genre.ROMANTISK, 195, Format.FORMAT_2D, AgeLimit.ELLEVE_PLUS, Language.ENGELSK));

                Movie movie10 = movieRepository.save(new Movie(null, "Parasite",
                        "Grådighed og klasseskel truer det nyligt dannede forhold mellem den velhavende Park-familie og den fattige Kim-klan.",
                        2019, Genre.ACTION, 132, Format.FORMAT_2D, AgeLimit.FEMTEN_PLUS, Language.ENGELSK));

                // Theaters
                Theater theater1 = theaterRepository.save(new Theater(null, "Stor sal", 20, 12));
                Theater theater2 = theaterRepository.save(new Theater(null, "Lille sal", 25, 16));

                // Seats
                createSeatsForTheater(theater1, seatRepository);
                createSeatsForTheater(theater2, seatRepository);

                // Generate showings for each movie (3-8 showings from March 18th 2026 onwards)
                Showing showing1 = showingRepository.save(new Showing(null, movie1, theater1, LocalDateTime.now().plusDays(5).withHour(19).withMinute(0), LocalDateTime.now().plusDays(5).withHour(21).withMinute(28), ShowingStatus.UPCOMING));
                Showing showing2 = showingRepository.save(new Showing(null, movie3, theater1, LocalDateTime.now().plusDays(5).withHour(22).withMinute(0), LocalDateTime.now().plusDays(5).withHour(23).withMinute(44), ShowingStatus.UPCOMING));
                Showing showing3 = showingRepository.save(new Showing(null, movie9, theater2, LocalDateTime.now().plusDays(6).withHour(18).withMinute(30), LocalDateTime.now().plusDays(6).withHour(21).withMinute(45), ShowingStatus.UPCOMING));
                showingRepository.save(new Showing(null, movie10, theater1, LocalDateTime.now().plusDays(6).withHour(21).withMinute(0), LocalDateTime.now().plusDays(6).withHour(22).withMinute(57), ShowingStatus.CANCELLED));
                showingRepository.save(new Showing(null, movie10, theater2, LocalDateTime.now().minusDays(1).withHour(21).withMinute(0), LocalDateTime.now().minusDays(1).withHour(22).withMinute(57), ShowingStatus.COMPLETED));

                List<Movie> allMovies = List.of(movie1, movie2, movie3, movie4, movie5, movie6, movie7, movie8, movie9, movie10);
                List<Theater> theaters = List.of(theater1, theater2);
                Random random = new Random();
                LocalDateTime baseDate = LocalDateTime.of(2026, 3, 18, 10, 0);

                for (Movie movie : allMovies) {
                    int numShowings = 3 + random.nextInt(6); // 3 to 8
                    for (int i = 0; i < numShowings; i++) {
                        // Spread showings over multiple days and different times
                        int dayOffset = random.nextInt(14); // Next 14 days
                        int hour = 14 + random.nextInt(8); // Between 14:00 and 22:00
                        int minute = random.nextBoolean() ? 0 : 30;

                        LocalDateTime startTime = baseDate.plusDays(dayOffset).withHour(hour).withMinute(minute);
                        LocalDateTime endTime = startTime.plusMinutes(movie.getDurationInMinutes());
                        Theater theater = theaters.get(random.nextInt(theaters.size()));

                        showingRepository.save(new Showing(null, movie, theater, startTime, endTime, ShowingStatus.UPCOMING));
                    }
                }

                // Opret Reservationer (Placeholders)
                // Hent sæder for at kunne knytte dem til reservationer
                List<Seat> theater1Seats = seatRepository.findAll().stream().filter(s -> s.getTheater().getTheaterId().equals(theater1.getTheaterId())).toList();
                List<Seat> theater2Seats = seatRepository.findAll().stream().filter(s -> s.getTheater().getTheaterId().equals(theater2.getTheaterId())).toList();

                User user1 = userRepository.save(new User(null, "Alice", LocalDate.of(1990, 5, 15), Role.CUSTOMER, "123"));
                User user2 = userRepository.save(new User(null, "Mikkel", LocalDate.of(1999, 10, 13), Role.EMPLOYEE, "123"));
                User user3 = userRepository.save(new User(null, "Sara", LocalDate.of(1921, 1, 1), Role.ADMIN, "123"));
                List<User> users = List.of(user1, user2, user3);

                // Reservation 1: Alice (2 sæder)
                Reservation reservation1 = new Reservation(null, showing1, user1, LocalDateTime.now(), 200.0, BookingStatus.CONFIRMED, PaymentStatus.PAID);
                reservation1.addReservedSeat(new ReservationSeat(null, reservation1, theater1Seats.get(0), 100.0)); // Sæde 1, Række 1
                reservation1.addReservedSeat(new ReservationSeat(null, reservation1, theater1Seats.get(1), 100.0)); // Sæde 2, Række 1
                reservationRepository.save(reservation1);

                // Reservation 2: Mikkel (1 sæde)
                Reservation reservation2 = new Reservation(null, showing2, user2, LocalDateTime.now(), 100.0, BookingStatus.CONFIRMED, PaymentStatus.AWAITING);
                reservation2.addReservedSeat(new ReservationSeat(null, reservation2, theater1Seats.get(2), 100.0)); // Sæde 3, Række 1
                reservationRepository.save(reservation2);

                // Reservation 3: Sara (3 sæder i den store sal)
                Reservation reservation3 = new Reservation(null, showing3, user3, LocalDateTime.now(), 300.0, BookingStatus.PENDING, PaymentStatus.AWAITING);
                reservation3.addReservedSeat(new ReservationSeat(null, reservation3, theater2Seats.get(0), 100.0));
                reservation3.addReservedSeat(new ReservationSeat(null, reservation3, theater2Seats.get(1), 100.0));
                reservation3.addReservedSeat(new ReservationSeat(null, reservation3, theater2Seats.get(2), 100.0));
                reservationRepository.save(reservation3);

                // Add random traffic to upcoming showings
                List<Showing> upcoming = showingRepository.findAll().stream()
                        .filter(s -> s.getShowingStatus() == ShowingStatus.UPCOMING)
                        .toList();

                for (int j = 0; j < 15; j++) {
                    Showing showing = upcoming.get(random.nextInt(upcoming.size()));
                    User user = users.get(random.nextInt(users.size()));
                    Theater theater = showing.getTheater();
                    List<Seat> theaterSeats = seatRepository.findByTheater_TheaterId(theater.getTheaterId());

                    int numTickets = 1 + random.nextInt(4);
                    Reservation res = new Reservation(null, showing, user, LocalDateTime.now(), 0.0, BookingStatus.CONFIRMED, PaymentStatus.AWAITING);

                    int startSeatIdx = random.nextInt(Math.max(1, theaterSeats.size() - numTickets));
                    for (int k = 0; k < numTickets; k++) {
                        Seat seat = theaterSeats.get(startSeatIdx + k);
                        // Simple check to avoid duplicate seats in same showing (best effort for dummy data)
                        boolean alreadyTaken = reservationSeatRepository.existsByReservation_Showing_ShowingIdAndSeat_SeatId(showing.getShowingId(), seat.getSeatId());
                        if (!alreadyTaken) {
                            res.addReservedSeat(new ReservationSeat(null, res, seat, 130.0));
                        }
                    }
                    if (!res.getReservedSeats().isEmpty()) {
                        res.setTotalPrice(res.getReservedSeats().size() * 130.0);
                        reservationRepository.save(res);
                    }
                }
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
