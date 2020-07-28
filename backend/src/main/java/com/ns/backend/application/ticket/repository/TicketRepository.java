package com.ns.backend.application.ticket.repository;

import com.ns.backend.application.ticket.models.Ticket;

import java.util.List;

public interface TicketRepository {

    Ticket addTicket(Ticket ticket);

    Ticket updateTicketByID(String ID, Ticket ticket);

    Ticket getTicketByID(String ID);

    List<Ticket> getAllTickets();

    List<Ticket> getTicketsForUser(String user_id);

    List<Ticket> getAllTicketsByAirline(String airline_id);

    List<Ticket> getAllAvailableTickets();

    List<Ticket> getReturnTicketsByID(String ID);

    Boolean deleteTicketByID(String ID, int version);

}
