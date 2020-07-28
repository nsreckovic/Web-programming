package com.ns.backend.application.flight.service;

import com.ns.backend.application.airline.models.Airline;
import com.ns.backend.application.airline.repository.AirlineRepository;
import com.ns.backend.application.airline.repository.AirlineRepositoryImplSQLite;
import com.ns.backend.application.airport.repository.AirportRepository;
import com.ns.backend.application.airport.repository.AirportRepositoryImplSQLite;
import com.ns.backend.application.flight.models.Flight;
import com.ns.backend.application.flight.models.request.FlightRequestDto;
import com.ns.backend.application.flight.models.response.FlightResponseDto;
import com.ns.backend.application.flight.repository.FlightRepository;
import com.ns.backend.application.flight.repository.FlightRepositoryImplSQLite;

import java.util.ArrayList;
import java.util.List;

public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;
    private AirportRepository airportRepository;
    private AirlineRepository airlineRepository;

    public FlightServiceImpl() {
        flightRepository = FlightRepositoryImplSQLite.getInstance();
        airportRepository = AirportRepositoryImplSQLite.getInstance();
        airlineRepository = AirlineRepositoryImplSQLite.getInstance();
    }

    @Override
    public Boolean isIDAvailable(String ID) {
        if (flightRepository.getFlightByID(ID) == null) return true;
        return false;
    }

    @Override
    public FlightResponseDto addFlight(FlightRequestDto flightRequestDto) {
        if (!flightRequestDto.checkData()) return null;

        if (airlineRepository.getAirlineByName(flightRequestDto.getAirline()) != null &&
            airportRepository.getAirportByID(flightRequestDto.getDeparture_airport_ID()) != null &&
            airportRepository.getAirportByID(flightRequestDto.getArrival_airport_ID()) != null) {

            Flight flight = new Flight(flightRequestDto.getId(),
                                       airlineRepository.getAirlineByName(flightRequestDto.getAirline()).getID().toString(),
                                       flightRequestDto.getDeparture_airport_ID(),
                                       flightRequestDto.getArrival_airport_ID());
            flight = flightRepository.addFlight(flight);

            if (flight != null) {
                Airline airline = airlineRepository.getAirlineByID(flight.getAirline_ID());
                return new FlightResponseDto(flight.getID(),
                                             airline.getName(),
                                             airline.getLink(),
                                             flight.getDeparture_airport_ID(),
                                             flight.getArrival_airport_ID());
            }
        }
        return null;
    }

    @Override
    public FlightResponseDto updateFlightByID(String ID, FlightRequestDto flightRequestDto) {
        if (!flightRequestDto.checkData()) return null;

        Flight flightToUpdate = flightRepository.getFlightByID(ID);
        if (flightToUpdate == null) return null;

        if (!flightRequestDto.getId().equals(flightToUpdate.getID()) ||
            !flightRequestDto.getAirline().equals(airlineRepository.getAirlineByID(flightToUpdate.getAirline_ID()).getName()) ||
            !flightRequestDto.getDeparture_airport_ID().equals(flightToUpdate.getDeparture_airport_ID()) ||
            !flightRequestDto.getArrival_airport_ID().equals(flightToUpdate.getArrival_airport_ID())) {

            flightToUpdate.setID(flightRequestDto.getId());
            flightToUpdate.setAirline_ID(airlineRepository.getAirlineByName(flightRequestDto.getAirline()).getID().toString());
            flightToUpdate.setDeparture_airport_ID(flightRequestDto.getDeparture_airport_ID());
            flightToUpdate.setArrival_airport_ID(flightRequestDto.getArrival_airport_ID());
            flightToUpdate.incrementVersion();

            Flight updatedFlight = flightRepository.updateFlightByID(ID, flightToUpdate);
            if (updatedFlight != null) {
                Airline airline = airlineRepository.getAirlineByID(updatedFlight.getAirline_ID());
                return new FlightResponseDto(updatedFlight.getID(),
                                             airline.getName(),
                                             airline.getLink(),
                                             updatedFlight.getDeparture_airport_ID(),
                                             updatedFlight.getArrival_airport_ID());
            }
            else return null;

        } else {
            Airline airline = airlineRepository.getAirlineByID(flightToUpdate.getAirline_ID());
            return new FlightResponseDto(flightToUpdate.getID(),
                                         airline.getName(),
                                         airline.getLink(),
                                         flightToUpdate.getDeparture_airport_ID(),
                                         flightToUpdate.getArrival_airport_ID());
        }
    }

    @Override
    public FlightResponseDto getFlightByID(String ID) {
        Flight flight = flightRepository.getFlightByID(ID);
        if (flight != null) {
            Airline airline = airlineRepository.getAirlineByID(flight.getAirline_ID());
            return new FlightResponseDto(flight.getID(),
                                         airline.getName(),
                                         airline.getLink(),
                                         flight.getDeparture_airport_ID(),
                                         flight.getArrival_airport_ID());
        }
        return null;
    }

    @Override
    public List<FlightResponseDto> getAllFlights() {
        List<Flight> flights = flightRepository.getAllFlights();
        List<FlightResponseDto> flightResponseDtos = makeFlightResponseDtos(flights);
        if (flightResponseDtos != null) return flightResponseDtos;
        return null;
    }

    @Override
    public List<FlightResponseDto> getFlightsByDepartureAirport(String departure_airport_ID) {
        if (airportRepository.getAirportByID(departure_airport_ID) == null) return null;
        List<Flight> flights = flightRepository.getFlightsByDepartureAirportID(departure_airport_ID);
        List<FlightResponseDto> flightResponseDtos = makeFlightResponseDtos(flights);
        if (flightResponseDtos != null) return flightResponseDtos;
        return null;
    }

    @Override
    public List<FlightResponseDto> getFlightsByArrivalAirport(String arrival_airport_ID) {
        if (airportRepository.getAirportByID(arrival_airport_ID) == null) return null;
        List<Flight> flights = flightRepository.getFlightsByArrivalAirportID(arrival_airport_ID);
        List<FlightResponseDto> flightResponseDtos = makeFlightResponseDtos(flights);
        if (flightResponseDtos != null) return flightResponseDtos;
        return null;
    }

    @Override
    public Boolean deleteFlightByID(String ID) {
        Flight flight = flightRepository.getFlightByID(ID);
        return flightRepository.deleteFlightByID(ID, flight.getVersion());
    }

    private List<FlightResponseDto> makeFlightResponseDtos(List<Flight> flights) {
        List<FlightResponseDto> flightResponseDtos = new ArrayList<>();
        if (flights != null) {
            for (Flight flight : flights) {
                Airline airline = airlineRepository.getAirlineByID(flight.getAirline_ID());
                flightResponseDtos.add(new FlightResponseDto(flight.getID(),
                                                             airline.getName(),
                                                             airline.getLink(),
                                                             flight.getDeparture_airport_ID(),
                                                             flight.getArrival_airport_ID()));
            }
            return flightResponseDtos;
        }
        return null;
    }
}
