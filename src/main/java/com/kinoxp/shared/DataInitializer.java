package com.kinoxp.shared;

import com.kinoxp.movie.*;
import com.kinoxp.reservation.*;
import com.kinoxp.seat.Seat;
import com.kinoxp.seat.SeatRepository;
import com.kinoxp.showing.Showing;
import com.kinoxp.showing.ShowingRepository;
import com.kinoxp.showing.ShowingStatus;
import com.kinoxp.theater.Theater;
import com.kinoxp.theater.TheaterRepository;
import com.kinoxp.user.Role;
import com.kinoxp.user.User;
import com.kinoxp.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
class DataInitializer {

    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;
    private final ShowingRepository showingRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final ReservationRepository reservationRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(TheaterRepository theaterRepository, SeatRepository seatRepository,
                           ShowingRepository showingRepository, ReservationSeatRepository reservationSeatRepository,
                           ReservationRepository reservationRepository, MovieRepository movieRepository,
                           UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.theaterRepository = theaterRepository;
        this.seatRepository = seatRepository;
        this.showingRepository = showingRepository;
        this.reservationSeatRepository = reservationSeatRepository;
        this.reservationRepository = reservationRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            migratePlaintextPasswordsToBcrypt();

            User user1 = ensureUserExists("Alice", Role.ADMIN, "pw");
            User user2 = ensureUserExists("Birthe", Role.CUSTOMER, "pw");
            User user3 = ensureUserExists("Mikkel", Role.EMPLOYEE, "pw");

            List<User> users = List.of(user1, user2, user3);

            if (movieRepository.count() == 0) {

                // --- Film ---
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

                // --- Sale ---
                Theater theater1 = theaterRepository.save(new Theater(null, "Stor sal", 20, 12));
                Theater theater2 = theaterRepository.save(new Theater(null, "Lille sal", 25, 16));

                createSeatsForTheater(theater1, seatRepository);
                createSeatsForTheater(theater2, seatRepository);

                // --- Specifikke visninger ---
                Showing showing1 = showingRepository.save(new Showing(null, movie1, theater1, LocalDateTime.now().plusDays(5).withHour(19).withMinute(0), LocalDateTime.now().plusDays(5).withHour(21).withMinute(28), ShowingStatus.UPCOMING));
                Showing showing2 = showingRepository.save(new Showing(null, movie3, theater1, LocalDateTime.now().plusDays(5).withHour(22).withMinute(0), LocalDateTime.now().plusDays(5).withHour(23).withMinute(44), ShowingStatus.UPCOMING));
                Showing showing3 = showingRepository.save(new Showing(null, movie9, theater2, LocalDateTime.now().plusDays(6).withHour(18).withMinute(30), LocalDateTime.now().plusDays(6).withHour(21).withMinute(45), ShowingStatus.UPCOMING));
                showingRepository.save(new Showing(null, movie10, theater1, LocalDateTime.now().plusDays(6).withHour(21).withMinute(0), LocalDateTime.now().plusDays(6).withHour(22).withMinute(57), ShowingStatus.CANCELLED));
                showingRepository.save(new Showing(null, movie10, theater2, LocalDateTime.now().minusDays(1).withHour(21).withMinute(0), LocalDateTime.now().minusDays(1).withHour(22).withMinute(57), ShowingStatus.COMPLETED));

                // --- Tilfældige visninger ---
                List<Movie> allMovies = List.of(movie1, movie2, movie3, movie4, movie5, movie6, movie7, movie8, movie9, movie10);
                List<Theater> theaters = List.of(theater1, theater2);
                Random random = new Random();
                LocalDateTime baseDate = LocalDateTime.of(2026, 3, 18, 10, 0);

                for (Movie movie : allMovies) {
                    int numShowings = 3 + random.nextInt(6);
                    for (int i = 0; i < numShowings; i++) {
                        int dayOffset = random.nextInt(14);
                        int hour = 14 + random.nextInt(8);
                        int minute = random.nextBoolean() ? 0 : 30;
                        LocalDateTime startTime = baseDate.plusDays(dayOffset).withHour(hour).withMinute(minute);
                        LocalDateTime endTime = startTime.plusMinutes(movie.getDurationInMinutes());
                        Theater theater = theaters.get(random.nextInt(theaters.size()));
                        showingRepository.save(new Showing(null, movie, theater, startTime, endTime, ShowingStatus.UPCOMING));
                    }
                }

                // --- Sæder pr. sal ---
                List<Seat> theater1Seats = seatRepository.findAll().stream()
                        .filter(s -> s.getTheater().getTheaterId().equals(theater1.getTheaterId()))
                        .toList();
                List<Seat> theater2Seats = seatRepository.findAll().stream()
                        .filter(s -> s.getTheater().getTheaterId().equals(theater2.getTheaterId()))
                        .toList();

                // --- Specifikke reservationer ---
                Reservation reservation1 = new Reservation(null, showing1, user1, LocalDateTime.now(), 200.0, BookingStatus.CONFIRMED, PaymentStatus.PAID);
                reservation1.addReservedSeat(new ReservationSeat(null, reservation1, theater1Seats.get(0), 100.0));
                reservation1.addReservedSeat(new ReservationSeat(null, reservation1, theater1Seats.get(1), 100.0));
                reservationRepository.save(reservation1);

                Reservation reservation2 = new Reservation(null, showing2, user2, LocalDateTime.now(), 100.0, BookingStatus.CONFIRMED, PaymentStatus.AWAITING);
                reservation2.addReservedSeat(new ReservationSeat(null, reservation2, theater1Seats.get(2), 100.0));
                reservationRepository.save(reservation2);

                Reservation reservation3 = new Reservation(null, showing3, user3, LocalDateTime.now(), 300.0, BookingStatus.PENDING, PaymentStatus.AWAITING);
                reservation3.addReservedSeat(new ReservationSeat(null, reservation3, theater2Seats.get(0), 100.0));
                reservation3.addReservedSeat(new ReservationSeat(null, reservation3, theater2Seats.get(1), 100.0));
                reservation3.addReservedSeat(new ReservationSeat(null, reservation3, theater2Seats.get(2), 100.0));
                reservationRepository.save(reservation3);

                // --- Tilfældige reservationer ---
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

            } // end if movieRepository.count() == 0
        };
    }

    private User ensureUserExists(String name, Role role, String rawPassword) {
        List<User> usersWithSameName = userRepository.findAllByNameIgnoreCaseOrderByUserIdAsc(name);
        if (!usersWithSameName.isEmpty()) {
            User existing = usersWithSameName.get(0);
            boolean needsUpdate = existing.getRole() != role || !passwordEncoder.matches(rawPassword, existing.getPassword());
            if (needsUpdate) {
                existing.setRole(role);
                existing.setPassword(passwordEncoder.encode(rawPassword));
                return userRepository.save(existing);
            }
            return existing;
        }

        User user = new User();
        user.setName(name);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }

    private void migratePlaintextPasswordsToBcrypt() {
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            String storedPassword = user.getPassword();
            if (storedPassword == null || storedPassword.isBlank()) {
                continue;
            }

            // BCrypt hashes start with $2a$, $2b$ or $2y$.
            if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
                continue;
            }

            user.setPassword(passwordEncoder.encode(storedPassword));
            userRepository.save(user);
        }
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