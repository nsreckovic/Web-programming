package com.ns.backend.application.flightInstance.service;

import com.ns.backend.application.flight.repository.FlightRepository;
import com.ns.backend.application.flight.repository.FlightRepositoryImplSQLite;
import com.ns.backend.application.flightInstance.models.FlightInstance;
import com.ns.backend.application.flightInstance.models.request.FlightInstanceRequestDto;
import com.ns.backend.application.flightInstance.models.response.FlightInstanceResponseDto;
import com.ns.backend.application.flightInstance.repository.FlightInstanceRepository;
import com.ns.backend.application.flightInstance.repository.FlightInstanceRepositoryImplSQLite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FlightInstanceServiceImpl implements FlightInstanceService {

    private FlightInstanceRepository flightInstanceRepository;
    private FlightRepository flightRepository;
    private SimpleDateFormat sdf;

    public FlightInstanceServiceImpl() {
        flightInstanceRepository = FlightInstanceRepositoryImplSQLite.getInstance();
        flightRepository = FlightRepositoryImplSQLite.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public FlightInstanceResponseDto addFlightInstance(FlightInstanceRequestDto flightInstanceRequestDto) {
        if (flightRepository.getFlightByID(flightInstanceRequestDto.getFlight_ID()) == null) return null;

        java.util.Date utilDate;
        java.sql.Date sqlDate;
        try { utilDate = sdf.parse(flightInstanceRequestDto.getDate()); }
        catch (ParseException e) { return null; }
        sqlDate = new java.sql.Date(utilDate.getTime());

        FlightInstance flightInstance = new FlightInstance(flightInstanceRequestDto.getFlight_ID(), sqlDate);
        flightInstance = flightInstanceRepository.addFlightInstance(flightInstance);

        if (flightInstance != null) return new FlightInstanceResponseDto(flightInstance);
        return null;
    }

    @Override
    public FlightInstanceResponseDto updateFlightInstance(String id, FlightInstanceRequestDto flightInstanceRequestDto) {
        FlightInstance current = flightInstanceRepository.getFlightInstanceByID(id);
        if (current == null) return null;

        current.setFlight_ID(flightInstanceRequestDto.getFlight_ID());
        java.util.Date utilDate;
        java.sql.Date sqlDate;
        try { utilDate = sdf.parse(flightInstanceRequestDto.getDate()); }
        catch (ParseException e) { return null; }
        sqlDate = new java.sql.Date(utilDate.getTime());
        current.setDate(sqlDate);
        current.incrementVersion();

        FlightInstance updated = flightInstanceRepository.updateFlightInstance(id, current);
        if (updated != null) return new FlightInstanceResponseDto(updated);
        return null;
    }

    @Override
    public List<FlightInstanceResponseDto> getAll() {
        List<FlightInstance> flightInstances = flightInstanceRepository.getAll();
        List<FlightInstanceResponseDto> flightInstanceResponseDtos = new ArrayList<>();
        if (flightInstances != null) {
            for (FlightInstance flightInstance : flightInstances) {
                flightInstanceResponseDtos.add(new FlightInstanceResponseDto(flightInstance));
            }
            return flightInstanceResponseDtos;
        }
        return null;
    }

    @Override
    public Boolean deleteFlightInstanceByID(String ID) {
        FlightInstance flightInstance = flightInstanceRepository.getFlightInstanceByID(ID);
        return flightInstanceRepository.deleteFlightInstanceByID(ID, flightInstance.getVersion());
    }
}
