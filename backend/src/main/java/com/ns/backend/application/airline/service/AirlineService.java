package com.ns.backend.application.airline.service;

import com.ns.backend.application.airline.models.request.AirlineRequestDto;
import com.ns.backend.application.airline.models.response.AirlineResponseDto;

import java.util.List;

public interface AirlineService {

    Boolean isNameAvailable(String name);

    AirlineResponseDto addAirline(AirlineRequestDto airlineRequestDto);

    AirlineResponseDto updateAirline(String airline_id, AirlineRequestDto airlineRequestDto);

    AirlineResponseDto getAirlineByID(String airline_id);

    List<AirlineResponseDto> getAllAirlines();

    Boolean deleteAirlineByID(String airline_id);

}
