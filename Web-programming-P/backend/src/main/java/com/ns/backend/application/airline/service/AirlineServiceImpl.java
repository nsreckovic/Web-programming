package com.ns.backend.application.airline.service;

import com.ns.backend.application.airline.models.Airline;
import com.ns.backend.application.airline.models.request.AirlineRequestDto;
import com.ns.backend.application.airline.models.response.AirlineResponseDto;
import com.ns.backend.application.airline.repository.AirlineRepository;
import com.ns.backend.application.airline.repository.AirlineRepositoryImplSQLite;

import java.util.ArrayList;
import java.util.List;

public class AirlineServiceImpl implements AirlineService {

    private AirlineRepository airlineRepository;

    public AirlineServiceImpl() {
        airlineRepository = AirlineRepositoryImplSQLite.getInstance();
    }

    @Override
    public Boolean isNameAvailable(String name) {
        if (airlineRepository.getAirlineByName(name) == null) return true;
        return false;
    }

    @Override
    public AirlineResponseDto addAirline(AirlineRequestDto airlineRequestDto) {
        Airline airline = airlineRepository.addAirline(new Airline(airlineRequestDto));
        if (airline != null) return new AirlineResponseDto(airline);
        return null;
    }

    @Override
    public AirlineResponseDto updateAirline(String airline_id, AirlineRequestDto airlineRequestDto) {
        Airline airlineToUpdate = airlineRepository.getAirlineByID(airline_id);
        if (airlineToUpdate == null) return null;
        airlineToUpdate.setName(airlineRequestDto.getName());
        airlineToUpdate.setLink(airlineRequestDto.getLink());
        airlineToUpdate.incrementVersion();

        Airline updatedAirline = airlineRepository.updateAirlineByID(airline_id, airlineToUpdate);
        if (updatedAirline != null) return new AirlineResponseDto(updatedAirline);
        return null;
    }

    @Override
    public AirlineResponseDto getAirlineByID(String airline_id) {
        Airline airline = airlineRepository.getAirlineByID(airline_id);
        if (airline != null) return new AirlineResponseDto(airline);
        return null;
    }

    @Override
    public List<AirlineResponseDto> getAllAirlines() {
        List<Airline> airlines = airlineRepository.getAllAirlines();
        List<AirlineResponseDto> airlineResponseDtos = new ArrayList<>();
        if (airlines != null) {
            for (Airline airline : airlines) {
                airlineResponseDtos.add(new AirlineResponseDto(airline));
            }
            return airlineResponseDtos;
        }
        return null;
    }

    @Override
    public Boolean deleteAirlineByID(String airline_id) {
        Airline airline = airlineRepository.getAirlineByID(airline_id);
        return airlineRepository.deleteAirlineByID(airline_id, airline.getVersion());
    }

}
