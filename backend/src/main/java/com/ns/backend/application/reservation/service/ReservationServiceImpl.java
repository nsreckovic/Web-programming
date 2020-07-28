package com.ns.backend.application.reservation.service;

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
import com.ns.backend.application.reservation.models.Reservation;
import com.ns.backend.application.reservation.models.request.ReservationFilterRequestDto;
import com.ns.backend.application.reservation.models.request.ReservationRequestDto;
import com.ns.backend.application.reservation.models.response.ReservationResponseDto;
import com.ns.backend.application.reservation.repository.ReservationRepository;
import com.ns.backend.application.reservation.repository.ReservationRepositoryImplSQLite;
import com.ns.backend.application.ticket.models.Ticket;
import com.ns.backend.application.ticket.repository.TicketRepository;
import com.ns.backend.application.ticket.repository.TicketRepositoryImplSQLite;
import com.ns.backend.application.user.models.User;
import com.ns.backend.application.user.models.UserType;
import com.ns.backend.application.user.repository.UserRepository;
import com.ns.backend.application.user.repository.UserRepositoryImplSQLite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {
    private ReservationRepository reservationRepository;
    private TicketRepository ticketRepository;
    private FlightInstanceRepository flightInstanceRepository;
    private FlightRepository flightRepository;
    private AirportRepository airportRepository;
    private AirlineRepository airlineRepository;
    private UserRepository userRepository;
    private SimpleDateFormat sdf;
    private SimpleDateFormat sdfFull;

    public ReservationServiceImpl() {
        reservationRepository = ReservationRepositoryImplSQLite.getInstance();
        ticketRepository = TicketRepositoryImplSQLite.getInstance();
        flightInstanceRepository = FlightInstanceRepositoryImplSQLite.getInstance();
        flightRepository = FlightRepositoryImplSQLite.getInstance();
        airportRepository = AirportRepositoryImplSQLite.getInstance();
        airlineRepository = AirlineRepositoryImplSQLite.getInstance();
        userRepository = UserRepositoryImplSQLite.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public Boolean addReservation(ReservationRequestDto reservationRequestDto) {
        Reservation reservation = new Reservation(reservationRequestDto);
        reservation = reservationRepository.addReservation(reservation);

        if (reservation != null) {
            Ticket departure_ticket = ticketRepository.getTicketByID(reservation.getDeparture_ticket_ID());
            if (departure_ticket == null) return false;
            if (departure_ticket.decrementCount()) {
                departure_ticket.incrementVersion();
                ticketRepository.updateTicketByID(departure_ticket.getID().toString(), departure_ticket);
            } else return false;

            if (reservation.getReturn_ticket_ID() != null) {
                Ticket return_ticket = ticketRepository.getTicketByID(reservation.getReturn_ticket_ID());
                if (return_ticket == null) return false;
                if (return_ticket.decrementCount()) {
                    return_ticket.incrementVersion();
                    ticketRepository.updateTicketByID(return_ticket.getID().toString(), return_ticket);
                } else return false;
            }

            return true;
        }
        return false;
    }

    @Override
    public Boolean updateReservationByID(String ID, ReservationRequestDto rDto) {
        if (!rDto.checkData()) return null;
        Reservation r = reservationRepository.getReservationByID(ID);

        if (rDto.getReturn_ticket_ID() == null) {
            if (r.getDeparture_ticket_ID().equals(rDto.getDeparture_ticket_ID()) &&
                    r.getUser_ID().equals(rDto.getUser_ID())) return true;
        } else {
            if (r.getDeparture_ticket_ID().equals(rDto.getDeparture_ticket_ID()) &&
                    r.getReturn_ticket_ID().equals(rDto.getReturn_ticket_ID()) &&
                    r.getUser_ID().equals(rDto.getUser_ID())) return true;
        }


        r.setDate(rDto.getSqlDate());
        r.incrementVersion();

        if (!r.getDeparture_ticket_ID().equals(rDto.getDeparture_ticket_ID())) {
            Ticket t = ticketRepository.getTicketByID(r.getDeparture_ticket_ID());
            if (t != null) {
                t.incrementCount();
                t.incrementVersion();
                ticketRepository.updateTicketByID(t.getID().toString(), t);

                Ticket newT = ticketRepository.getTicketByID(rDto.getDeparture_ticket_ID());
                if (newT != null) {
                    if (newT.decrementCount()) {
                        newT.incrementVersion();
                        ticketRepository.updateTicketByID(newT.getID().toString(), newT);
                        r.setDeparture_ticket_ID(rDto.getDeparture_ticket_ID());
                    } else return false;
                } else return null;
            }  else return null;
        }

        if (rDto.getReturn_ticket_ID() != null && !r.getReturn_ticket_ID().equals(rDto.getReturn_ticket_ID())) {
            Ticket t = ticketRepository.getTicketByID(r.getReturn_ticket_ID());
            if (t != null) {
                t.incrementCount();
                t.incrementVersion();
                ticketRepository.updateTicketByID(t.getID().toString(), t);

                Ticket newT = ticketRepository.getTicketByID(rDto.getReturn_ticket_ID());
                if (newT != null) {
                    if (newT.decrementCount()) {
                        newT.incrementVersion();
                        ticketRepository.updateTicketByID(newT.getID().toString(), newT);
                        r.setReturn_ticket_ID(rDto.getReturn_ticket_ID());
                    } else return false;
                } else return null;
            }  else return null;
        }

        if (!r.getUser_ID().equals(rDto.getUser_ID())) r.setUser_ID(rDto.getUser_ID());

        Reservation updated = reservationRepository.updateReservationByID(ID, r);
        if (updated != null) return true;
        return false;
    }

    @Override
    public ReservationResponseDto getReservationByID(String ID) {
        Reservation r = reservationRepository.getReservationByID(ID);
        if (r != null ) return makeResponse(r);
        return null;
    }

    @Override
    public ReservationResponseDto getReservationByIDforCustomer(String username, String ID) {
        Reservation r = reservationRepository.getReservationByID(ID);
        if (r != null ) {
            User u = userRepository.getUserByUsername(username);
            if (u.getID().toString().equals(r.getUser_ID())) return makeResponse(r);
        }
        return null;
    }

    @Override
    public List<ReservationResponseDto> getAllReservations(ReservationFilterRequestDto filter) {
        List<Reservation> reservations;
        if (filter.getUsername() == null) reservations = reservationRepository.getAllReservations();
        else {
            User user = userRepository.getUserByUsername(filter.getUsername());
            reservations = reservationRepository.getAllReservationsByUserID(user.getID().toString());
        }
        List<ReservationResponseDto> preliminaryResponses = makeResponses(reservations);
        if (filter.allNull()) return preliminaryResponses;

        for (int i = 0; i < preliminaryResponses.size(); i++) {
            ReservationResponseDto r = preliminaryResponses.get(i);

            if (filter.getType() != null && filter.getType().equals("One-way")) {
                if (r.getReturn_ticket_id() != null) {
                    preliminaryResponses.remove(r);
                    i--;
                    continue;
                }
            }

            if (filter.getType() != null && filter.getType().equals("Round-trip")) {
                if (r.getReturn_ticket_id() == null) {
                    preliminaryResponses.remove(r);
                    i--;
                    continue;
                }
            }

            if (filter.getAirline() != null) {
                if (r.getReturn_airline() == null) {
                    if (!r.getAirline().equals(filter.getAirline())) {
                        preliminaryResponses.remove(r);
                        i--;
                        continue;
                    }
                } else {
                    if (!r.getAirline().equals(filter.getAirline()) && !r.getReturn_airline().equals(filter.getAirline())) {
                        preliminaryResponses.remove(r);
                        i--;
                        continue;
                    }
                }

            }
            if (filter.getFrom_filter() != null) {
                if (!r.getFrom_id().equals(filter.getFrom_filter())) {
                    preliminaryResponses.remove(r);
                    i--;
                    continue;
                }
            }
            if (filter.getTo_filter() != null) {
                if (!r.getTo_id().equals(filter.getTo_filter())) {
                    preliminaryResponses.remove(r);
                    i--;
                    continue;
                }
            }
            if (filter.getFrom_date_filter() != null && filter.getTo_date_filter() != null) {
                try {
                    Date fromDateFilter = sdf.parse(filter.getFrom_date_filter());
                    Date toDateFilter = sdf.parse(filter.getTo_date_filter());
                    Date departureDate = sdf.parse(r.getDeparture_date());
                    Date returnDate;
                    if (r.getReturn_date() != null) returnDate = sdf.parse(r.getReturn_date());
                    else returnDate = departureDate;
                    if (!((departureDate.after(fromDateFilter) && departureDate.before(toDateFilter)) &&
                        (returnDate.after(fromDateFilter) && returnDate.before(toDateFilter)))) {
                        preliminaryResponses.remove(r);
                        i--;
                        continue;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (filter.getFrom_date_filter() != null) {
                try {
                    Date fromDateFilter = sdf.parse(filter.getFrom_date_filter());
                    Date departureDate = sdf.parse(r.getDeparture_date());
                    if (!departureDate.after(fromDateFilter)) {
                        preliminaryResponses.remove(r);
                        i--;
                        continue;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (filter.getTo_date_filter() != null) {
                try {
                    Date toDateFilter = sdf.parse(filter.getTo_date_filter());
                    Date departureDate = sdf.parse(r.getDeparture_date());
                    Date returnDate;
                    if (r.getReturn_date() != null) returnDate = sdf.parse(r.getReturn_date());
                    else returnDate = departureDate;
                    if (!returnDate.before(toDateFilter)) {
                        preliminaryResponses.remove(r);
                        i--;
                        continue;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        }

        return preliminaryResponses;
    }

    private List<ReservationResponseDto> makeResponses(List<Reservation> reservations) {
        if (reservations == null) return null;
        List<ReservationResponseDto> reservationResponseDtos = new ArrayList<>();
        for (Reservation r : reservations) {
            ReservationResponseDto rRDto = makeResponse(r);
            reservationResponseDtos.add(rRDto);
        }
        return reservationResponseDtos;
    }

    private ReservationResponseDto makeResponse(Reservation r) {
        ReservationResponseDto rRDto = new ReservationResponseDto();

        Ticket from_ticket = ticketRepository.getTicketByID(r.getDeparture_ticket_ID());

        FlightInstance from_fi = flightInstanceRepository.getFlightInstanceByID(from_ticket.getFlight_instance_ID());
        Flight from_f = flightRepository.getFlightByID(from_fi.getFlight_ID());
        Airport dep_airport = airportRepository.getAirportByID(from_f.getDeparture_airport_ID());
        Airport arr_airport = airportRepository.getAirportByID(from_f.getArrival_airport_ID());
        Airline from_aline = airlineRepository.getAirlineByID(from_f.getAirline_ID());
        User user = userRepository.getUserByID(r.getUser_ID());

        rRDto.setId(r.getID().toString());
        rRDto.setReservation_date(r.getDate().toString());
        rRDto.setFrom_ticket_id(from_ticket.getID().toString());
        rRDto.setUser_id(r.getUser_ID());
        rRDto.setUsername(user.getUsername());
        rRDto.setFrom(dep_airport.getPlace());
        rRDto.setFrom_id(dep_airport.getID());
        rRDto.setTo(arr_airport.getPlace());
        rRDto.setTo_id(arr_airport.getID());
        rRDto.setDeparture_date(from_fi.getDate().toString());
        rRDto.setFlight_id(from_f.getID());
        rRDto.setAirline(from_aline.getName());
        rRDto.setAirline_link(from_aline.getLink());

        if (r.getReturn_ticket_ID() != null) {
            Ticket return_ticket = ticketRepository.getTicketByID(r.getReturn_ticket_ID());
            FlightInstance return_fi = flightInstanceRepository.getFlightInstanceByID(return_ticket.getFlight_instance_ID());
            Flight return_f = flightRepository.getFlightByID(return_fi.getFlight_ID());
            Airline return_aline = airlineRepository.getAirlineByID(return_f.getAirline_ID());

            rRDto.setReturn_ticket_id(return_ticket.getID().toString());
            rRDto.setReturn_date(return_fi.getDate().toString());
            rRDto.setReturn_flight_id(return_f.getID());
            rRDto.setReturn_airline(return_aline.getName());
            rRDto.setReturn_airline_link(return_aline.getLink());
        }

        return rRDto;
    }

    @Override
    public Boolean deleteReservationByID(String username, String ID) {
        User user = userRepository.getUserByUsername(username);
        if (user != null) {
            Reservation r = reservationRepository.getReservationByID(ID);
            if (r != null) {
                Ticket from_ticket = ticketRepository.getTicketByID(r.getDeparture_ticket_ID());
                Ticket to_ticket = ticketRepository.getTicketByID(r.getReturn_ticket_ID());
                if (from_ticket != null) {
                    from_ticket.incrementCount();
                    from_ticket.incrementVersion();
                    ticketRepository.updateTicketByID(r.getDeparture_ticket_ID(),from_ticket);
                } else return null;
                if (to_ticket != null) {
                    to_ticket.incrementCount();
                    to_ticket.incrementVersion();
                    ticketRepository.updateTicketByID(r.getReturn_ticket_ID(), to_ticket);
                }
                if (user.getType().equals(UserType.ADMIN)) {
                    return reservationRepository.deleteReservationByID(ID, r.getVersion());
                } else if (user.getType().equals(UserType.CUSTOMER)) {
                    if (r.getUser_ID().equals(user.getID().toString())) {
                        return reservationRepository.deleteReservationByID(ID, r.getVersion());
                    }
                }
            }
        }
        return null;
    }
}
