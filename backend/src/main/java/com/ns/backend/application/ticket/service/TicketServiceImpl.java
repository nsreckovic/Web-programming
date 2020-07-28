package com.ns.backend.application.ticket.service;

import com.ns.backend.application.airline.models.Airline;
import com.ns.backend.application.airline.repository.AirlineRepository;
import com.ns.backend.application.airline.repository.AirlineRepositoryImplSQLite;
import com.ns.backend.application.airport.models.Airport;
import com.ns.backend.application.airport.repository.AirportRepository;
import com.ns.backend.application.airport.repository.AirportRepositoryImplSQLite;
import com.ns.backend.application.flight.models.Flight;
import com.ns.backend.application.flight.repository.FlightRepository;
import com.ns.backend.application.flight.repository.FlightRepositoryImplSQLite;
import com.ns.backend.application.flightInstance.models.FlightInstance;
import com.ns.backend.application.flightInstance.repository.FlightInstanceRepository;
import com.ns.backend.application.flightInstance.repository.FlightInstanceRepositoryImplSQLite;
import com.ns.backend.application.ticket.models.Ticket;
import com.ns.backend.application.ticket.models.request.FromFilterRequestDto;
import com.ns.backend.application.ticket.models.request.ReturnFilterRequestDto;
import com.ns.backend.application.ticket.models.request.TicketRequestDto;
import com.ns.backend.application.ticket.models.response.TicketResponseDto;
import com.ns.backend.application.ticket.repository.TicketRepository;
import com.ns.backend.application.ticket.repository.TicketRepositoryImplSQLite;
import com.ns.backend.application.user.models.User;
import com.ns.backend.application.user.repository.UserRepository;
import com.ns.backend.application.user.repository.UserRepositoryImplSQLite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TicketServiceImpl implements TicketService {
    private TicketRepository ticketRepository;
    private FlightInstanceRepository flightInstanceRepository;
    private FlightRepository flightRepository;
    private AirportRepository airportRepository;
    private AirlineRepository airlineRepository;
    private UserRepository userRepository;
    private SimpleDateFormat sdf;

    public TicketServiceImpl() {
        ticketRepository = TicketRepositoryImplSQLite.getInstance();
        flightInstanceRepository = FlightInstanceRepositoryImplSQLite.getInstance();
        flightRepository = FlightRepositoryImplSQLite.getInstance();
        airportRepository = AirportRepositoryImplSQLite.getInstance();
        airlineRepository = AirlineRepositoryImplSQLite.getInstance();
        userRepository = UserRepositoryImplSQLite.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public TicketResponseDto addTicket(TicketRequestDto ticketRequestDto) {
        if (!ticketRequestDto.checkData()) return null;

        FlightInstance flightInstance = flightInstanceRepository.getFlightInstanceByID(ticketRequestDto.getFlight_instance_ID());
        if (flightInstance == null) return null;

        Ticket ticket = new Ticket(ticketRequestDto);
        ticket = ticketRepository.addTicket(ticket);

        if (ticket != null) return new TicketResponseDto();
        return null;
    }

    @Override
    public TicketResponseDto updateTicketByID(String ID, TicketRequestDto ticketRequestDto) {
        if (!ticketRequestDto.checkData()) return null;

        Ticket ticketToUpdate = ticketRepository.getTicketByID(ID);
        if (ticketToUpdate == null) return null;

        System.out.println("In dB: " + ticketToUpdate.getCount());
        System.out.println("New: " + ticketRequestDto.getCount());

        if (!ticketRequestDto.getFlight_instance_ID().equals(ticketToUpdate.getFlight_instance_ID()) ||
                ticketRequestDto.getCount() != ticketToUpdate.getCount()) {

            ticketToUpdate.setFlight_instance_ID(ticketRequestDto.getFlight_instance_ID());
            ticketToUpdate.setCount(ticketRequestDto.getCount());
            ticketToUpdate.incrementVersion();

            Ticket updatedTicket = ticketRepository.updateTicketByID(ID, ticketToUpdate);
            if (updatedTicket != null) return new TicketResponseDto();
            else return null;
        } else {
            return new TicketResponseDto();
        }
    }

    @Override
    public TicketResponseDto getTicketByID(String ID) {
        Ticket ticket = ticketRepository.getTicketByID(ID);
        if (ticket != null) {
            FlightInstance flightInstance = flightInstanceRepository.getFlightInstanceByID(ticket.getFlight_instance_ID());
            if (flightInstance != null) {
                String date = flightInstance.getDate().toString();
                Flight flight = flightRepository.getFlightByID(flightInstance.getFlight_ID());
                if (flight != null) {
                    Airline airline = airlineRepository.getAirlineByID(flight.getAirline_ID());
                    Airport departure_airport = airportRepository.getAirportByID(flight.getDeparture_airport_ID());
                    Airport arrival_airport = airportRepository.getAirportByID(flight.getArrival_airport_ID());
                    if (airline != null && departure_airport != null && arrival_airport != null) {
                        return new TicketResponseDto(ticket.getID().toString(),
                                ticket.getFlight_instance_ID(),
                                ticket.getCount(),
                                flight.getID(),
                                departure_airport.getPlace(),
                                departure_airport.getName(),
                                departure_airport.getID(),
                                arrival_airport.getPlace(),
                                arrival_airport.getName(),
                                arrival_airport.getID(),
                                date,
                                airline.getName(),
                                airline.getLink());
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<TicketResponseDto> getAllTickets() {
        List<Ticket> tickets = ticketRepository.getAllTickets();
        return makeResponses(tickets);
    }

    @Override
    public List<TicketResponseDto> getTicketsForUser(String username) {
        User user = userRepository.getUserByUsername(username);
        if (user != null) {
            List<Ticket> tickets = ticketRepository.getTicketsForUser(user.getID().toString());
            return makeResponses(tickets);
        }
        return null;
    }

    @Override
    public List<TicketResponseDto> getAllTicketsByAirline(String airline_id) {
        List<Ticket> tickets = ticketRepository.getAllTicketsByAirline(airline_id);
        return makeResponses(tickets);
    }

    @Override
    public List<TicketResponseDto> getDepartureFilteredTickets(FromFilterRequestDto filter) {
        List<Ticket> tickets = ticketRepository.getAllAvailableTickets();
        List<TicketResponseDto> preliminaryResponses = makeResponses(tickets);
        if (filter.allNull()) return preliminaryResponses;

        for (int i = 0; i < preliminaryResponses.size(); i++) {
            TicketResponseDto tRDto = preliminaryResponses.get(i);

            if (filter.getCurrent_departure_ticket_id() != null) {
                if (tRDto.getTicket_ID().equals(filter.getCurrent_departure_ticket_id())) {
                    preliminaryResponses.remove(tRDto);
                    i--;
                    continue;
                }
            }
            if (filter.getAirline() != null) {
                if (!tRDto.getAirline().equals(filter.getAirline())) {
                    preliminaryResponses.remove(tRDto);
                    i--;
                    continue;
                }
            }
            if (filter.getFrom_filter() != null) {
                if (!tRDto.getDeparture_airport_ID().equals(filter.getFrom_filter())) {
                    preliminaryResponses.remove(tRDto);
                    i--;
                    continue;
                }
            }
            if (filter.getTo_filter() != null) {
                if (!tRDto.getArrival_airport_ID().equals(filter.getTo_filter())) {
                    preliminaryResponses.remove(tRDto);
                    i--;
                    continue;
                }
            }
            if (filter.getFrom_date_filter() != null && filter.getTo_date_filter() != null) {
                try {
                    Date fromDateFilter = sdf.parse(filter.getFrom_date_filter());
                    Date toDateFilter = sdf.parse(filter.getTo_date_filter());
                    Date departureDate = sdf.parse(tRDto.getDeparture_date());
                    if (departureDate.before(fromDateFilter) && departureDate.after(toDateFilter)) {
                        preliminaryResponses.remove(tRDto);
                        i--;
                        continue;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (filter.getFrom_date_filter() != null) {
                try {
                    Date fromDateFilter = sdf.parse(filter.getFrom_date_filter());
                    Date departureDate = sdf.parse(tRDto.getDeparture_date());
                    if (departureDate.before(fromDateFilter)) {
                        preliminaryResponses.remove(tRDto);
                        i--;
                        continue;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (filter.getTo_date_filter() != null) {
                try {
                    Date toDateFilter = sdf.parse(filter.getTo_date_filter());
                    Date departureDate = sdf.parse(tRDto.getDeparture_date());
                    if (departureDate.after(toDateFilter)) {
                        preliminaryResponses.remove(tRDto);
                        i--;
                        continue;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
        if (!preliminaryResponses.isEmpty()) return preliminaryResponses;
        return null;
    }

    @Override
    public List<TicketResponseDto> getReturnFilteredTickets(ReturnFilterRequestDto filter) {
        List<Ticket> tickets = ticketRepository.getReturnTicketsByID(filter.getCurrent_departure_ticket_id());

        List<TicketResponseDto> preliminaryResponses = makeResponses(tickets);
        if (filter.allNull() || preliminaryResponses == null) return preliminaryResponses;

        for (int i = 0; i < preliminaryResponses.size(); i++) {
            TicketResponseDto tRDto = preliminaryResponses.get(i);

            if (filter.getCurrent_return_ticket_id() != null) {
                if (tRDto.getTicket_ID().equals(filter.getCurrent_return_ticket_id())) {
                    preliminaryResponses.remove(tRDto);
                    i--;
                    continue;
                }
            }
            if (filter.getAirline() != null) {
                if (!tRDto.getAirline().equals(filter.getAirline())) {
                    preliminaryResponses.remove(tRDto);
                    i--;
                    continue;
                }
            }
            if (filter.getTo_date_filter() != null) {
                try {
                    Date toDateFilter = sdf.parse(filter.getTo_date_filter());
                    Date departureDate = sdf.parse(tRDto.getDeparture_date());
                    if (departureDate.after(toDateFilter)) {
                        preliminaryResponses.remove(tRDto);
                        i--;
                        continue;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!preliminaryResponses.isEmpty()) return preliminaryResponses;
        return null;
    }

    @Override
    public List<TicketResponseDto> getAllAvailableTickets() {
        List<Ticket> tickets = ticketRepository.getAllAvailableTickets();
        return makeResponses(tickets);
    }

    @Override
    public List<TicketResponseDto> getReturnTicketsByID(String ID) {
        List<Ticket> tickets = ticketRepository.getReturnTicketsByID(ID);
        return makeResponses(tickets);
    }

    private List<TicketResponseDto> makeResponses(List<Ticket> tickets) {
        List<TicketResponseDto> ticketResponseDtos;
        if (tickets != null) {
            ticketResponseDtos = new ArrayList<>();
            for (Ticket ticket : tickets) {
                FlightInstance flightInstance = flightInstanceRepository.getFlightInstanceByID(ticket.getFlight_instance_ID());
                if (flightInstance != null) {
                    String date = flightInstance.getDate().toString();
                    Flight flight = flightRepository.getFlightByID(flightInstance.getFlight_ID());
                    if (flight != null) {
                        Airline airline = airlineRepository.getAirlineByID(flight.getAirline_ID());
                        Airport departure_airport = airportRepository.getAirportByID(flight.getDeparture_airport_ID());
                        Airport arrival_airport = airportRepository.getAirportByID(flight.getArrival_airport_ID());
                        if (airline != null && departure_airport != null && arrival_airport != null) {
                            ticketResponseDtos.add(new TicketResponseDto(ticket.getID().toString(),
                                    ticket.getFlight_instance_ID(),
                                    ticket.getCount(),
                                    flight.getID(),
                                    departure_airport.getPlace(),
                                    departure_airport.getName(),
                                    departure_airport.getID(),
                                    arrival_airport.getPlace(),
                                    arrival_airport.getName(),
                                    arrival_airport.getID(),
                                    date,
                                    airline.getName(),
                                    airline.getLink()));
                        }
                    }
                }
            }
            return ticketResponseDtos;
        }
        return null;
    }

    @Override
    public Boolean deleteTicketByID(String ID) {
        Ticket ticket = ticketRepository.getTicketByID(ID);
        return ticketRepository.deleteTicketByID(ID, ticket.getVersion());
    }
}
