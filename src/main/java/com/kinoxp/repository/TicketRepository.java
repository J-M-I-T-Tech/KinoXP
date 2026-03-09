package com.kinoxp.repository;

import com.kinoxp.model.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {}
