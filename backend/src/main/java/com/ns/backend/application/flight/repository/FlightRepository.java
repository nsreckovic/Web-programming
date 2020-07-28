package com.ns.backend.application.flight.repository;

import com.ns.backend.application.flight.models.Flight;

import java.util.List;

public interface FlightRepository {

    Flight addFlight(Flight flight);

    Flight updateFlightByID(String ID, Flight newFlight);

    Flight getFlightByID(String ID);

    Flight getFlightByName(String name);

    List<Flight> getAllFlights();

    List<Flight> getFlightsByDepartureAirportID(String departure_airport_ID);

    List<Flight> getFlightsByArrivalAirportID(String arrival_airport_ID);

    Boolean deleteFlightByID(String ID, int version);

}
