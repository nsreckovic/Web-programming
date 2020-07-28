package com.ns.backend.application.flight.service;

import com.ns.backend.application.flight.models.request.FlightRequestDto;
import com.ns.backend.application.flight.models.response.FlightResponseDto;
import com.ns.backend.application.flight.models.request.FlightRequestDto;
import com.ns.backend.application.flight.models.response.FlightResponseDto;

import java.util.List;

public interface FlightService {

    Boolean isIDAvailable(String ID);

    FlightResponseDto addFlight(FlightRequestDto flight);

    FlightResponseDto updateFlightByID(String ID, FlightRequestDto flight);

    FlightResponseDto getFlightByID(String ID);

    List<FlightResponseDto> getAllFlights();

    List<FlightResponseDto> getFlightsByDepartureAirport(String departure_airport_ID);

    List<FlightResponseDto> getFlightsByArrivalAirport(String arrival_airport_ID);

    Boolean deleteFlightByID(String ID);

}
