package com.ns.backend.application.airport.service;

import com.ns.backend.application.airport.models.request.AirportRequestDto;
import com.ns.backend.application.airport.models.response.AirportResponseDto;

import java.util.List;

public interface AirportService {

    Boolean isIDAvailable(String ID);

    AirportResponseDto addAirport(AirportRequestDto airportRequestDto);

    AirportResponseDto updateAirport(String name, AirportRequestDto airportRequestDto);

    AirportResponseDto getAirportByID(String ID);

    AirportResponseDto getAirportByName(String name);

    List<AirportResponseDto> getAllAirports();

    Boolean deleteAirportByID(String ID);

}
