package com.ns.backend.application.flightInstance.service;

import com.ns.backend.application.flightInstance.models.request.FlightInstanceRequestDto;
import com.ns.backend.application.flightInstance.models.response.FlightInstanceResponseDto;

import java.util.List;

public interface FlightInstanceService {

    FlightInstanceResponseDto addFlightInstance(FlightInstanceRequestDto flightInstanceRequestDto);

    FlightInstanceResponseDto updateFlightInstance(String id, FlightInstanceRequestDto flightInstanceRequestDto);

    List<FlightInstanceResponseDto> getAll();

    Boolean deleteFlightInstanceByID(String ID);

}
