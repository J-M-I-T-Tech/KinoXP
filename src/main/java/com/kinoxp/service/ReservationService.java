package com.kinoxp.service;

import com.kinoxp.dto.ReservationDTO;
import com.kinoxp.model.movie.Movie;
import com.kinoxp.model.reservation.Reservation;
import com.kinoxp.model.reservation.Status;
import com.kinoxp.model.seat.Seat;
import com.kinoxp.model.showing.Showing;
import com.kinoxp.model.user.User;
import com.kinoxp.repository.ReservationRepository;
import com.kinoxp.repository.ShowingRepository;
import com.kinoxp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ShowingRepository showingRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, ShowingRepository showingRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.showingRepository = showingRepository;

    }

    //Henter alle reservationer
    public List<Reservation> getAllReservation() {
        return reservationRepository.findAll();
    }

    //US: 3.6
    public double calculateMoviePrice(Movie movie, double standardPrice, double langFilmFee) {
        //logik: hvis en film er over 170 min. kommer der film gebyr på.
        if (movie.getDurationInMinutes() > 170) {
            return standardPrice + langFilmFee;
        }
        // her skal returnerer den normalprisen.
        return standardPrice;
    }

    //US:3.7
    public double calculateSeatPrice(Seat seat, double standardPrice, double rowFee) {
        // logik for hvis man køber en sæde efter række 7, er der gebyr på
        if (seat.getRowNumber() > 7) {
            return standardPrice + rowFee;
        }
        return standardPrice;
    }

//    private static final double RABAT_HVIS_MERE_END_10 = 0.07;
//
//    public double calculateWithDiscount(Reservation reservation) {
//        List<Ticket> tickets = reservation.getTickets();
//
//        double totalPrice = 0;
//        for (Ticket ticket : tickets) {
//            totalPrice += ticket.getPrice();
//        }
//
//        if (tickets.size() > 10) {
//            totalPrice *= (1 - RABAT_HVIS_MERE_END_10);
//        }
//
//        return totalPrice;
//    }

    //    US:3.2 Som kunde vil jeg have mængderabat, hvis jeg reserverer mere end 10 billetter.
    private static final double RABAT_HVIS_MERE_END_10 = 0.07;

    public double calculateWithDiscount(Movie movie, double standardPrice, double longFilmFee, double discount, int numberOfTickets) {
        //Pris pr billet
        double pricePerTicket = standardPrice;

        if (movie.getDurationInMinutes() > 170) {
            pricePerTicket += longFilmFee;
        }
        // total pris før rabat
        double totalPrice = pricePerTicket * numberOfTickets;

        if (numberOfTickets > 10) {
            totalPrice *= (1 - RABAT_HVIS_MERE_END_10);
        }
        return totalPrice;
    }

    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        User user = userRepository.findById(reservationDTO.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Showing showing = showingRepository.findById(reservationDTO.showingId())
                .orElseThrow(() -> new RuntimeException("Showing not found"));

        // laver tabeller
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setShowing(showing);
        reservation.setRowNumber(reservationDTO.rowNumber());
        reservation.setTotalPrice(reservationDTO.totalPrice());
        reservation.setCreated(LocalDateTime.now());
        reservation.setStatus(Status.CONFIRMED);

        Reservation savedReservation = reservationRepository.save(reservation);

        return convertToDTO(savedReservation);
    }

    public ReservationDTO convertToDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getShowing().getMovie().getTitle(),
                reservation.getTickets().size(),
                reservation.getTotalPrice(),
                reservation.getRowNumber(),
                reservation.getUser().getUserId(),
                reservation.getShowing().getShowingId()
        );
    }

    // alle reservationer
    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(this::convertToDTO)
                .toList();
    }

    // finde en reservation : specifik by id.
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElse(null);
    }

    //sletter en reservation by reservationId
    public void deleteReservation(Long reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new RuntimeException("Reservation not found");
        }
        reservationRepository.deleteById(reservationId);
    }
}
