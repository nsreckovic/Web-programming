package com.ns.backend.application.ticket.service;

import com.ns.backend.application.ticket.models.request.FromFilterRequestDto;
import com.ns.backend.application.ticket.models.request.ReturnFilterRequestDto;
import com.ns.backend.application.ticket.models.request.TicketRequestDto;
import com.ns.backend.application.ticket.models.response.TicketResponseDto;

import java.util.List;

public interface TicketService {

    TicketResponseDto addTicket(TicketRequestDto ticketRequestDto);

    TicketResponseDto updateTicketByID(String ID, TicketRequestDto ticketRequestDto);

    TicketResponseDto getTicketByID(String ID);

    List<TicketResponseDto> getAllTickets();

    List<TicketResponseDto> getTicketsForUser(String username);

    List<TicketResponseDto> getAllTicketsByAirline(String airline_id);

    List<TicketResponseDto> getDepartureFilteredTickets(FromFilterRequestDto filter);

    List<TicketResponseDto> getReturnFilteredTickets(ReturnFilterRequestDto filter);

    List<TicketResponseDto> getAllAvailableTickets();

    List<TicketResponseDto> getReturnTicketsByID(String ID);

    Boolean deleteTicketByID(String ID);

}
