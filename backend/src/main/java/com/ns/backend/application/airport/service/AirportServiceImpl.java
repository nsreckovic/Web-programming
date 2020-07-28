package com.ns.backend.application.airport.service;

import com.ns.backend.application.airport.models.Airport;
import com.ns.backend.application.airport.models.request.AirportRequestDto;
import com.ns.backend.application.airport.models.response.AirportResponseDto;
import com.ns.backend.application.airport.repository.AirportRepository;
import com.ns.backend.application.airport.repository.AirportRepositoryImplSQLite;

import java.util.ArrayList;
import java.util.List;

public class AirportServiceImpl implements AirportService {
    private AirportRepository airportRepository;

    public AirportServiceImpl() {
        airportRepository = AirportRepositoryImplSQLite.getInstance();
    }

    @Override
    public Boolean isIDAvailable(String ID) {
        if (airportRepository.getAirportByID(ID) == null) return true;
        return false;
    }

    @Override
    public AirportResponseDto addAirport(AirportRequestDto airportRequestDto) {
        if (!airportRequestDto.checkData()) return null;

        Airport airport = airportRepository.addAirport(new Airport(airportRequestDto));
        if (airport != null) return new AirportResponseDto(airport);
        return null;
    }

    @Override
    public AirportResponseDto updateAirport(String ID, AirportRequestDto airportRequestDto) {
        Airport airportToUpdate = airportRepository.getAirportByID(ID);
        if (airportToUpdate == null) return null;

        if (!airportRequestDto.getId().equals(airportToUpdate.getID()) ||
            !airportRequestDto.getName().equals(airportToUpdate.getName()) ||
            !airportRequestDto.getPlace().equals(airportToUpdate.getPlace())) {

            airportToUpdate.setID(airportRequestDto.getId());
            airportToUpdate.setName(airportRequestDto.getName());
            airportToUpdate.setPlace(airportRequestDto.getPlace());
            airportToUpdate.incrementVersion();

            Airport updatedAirport = airportRepository.updateAirportByID(ID, airportToUpdate);
            if (updatedAirport != null) return new AirportResponseDto(updatedAirport);
            else return null;
        } else {
            return new AirportResponseDto(airportToUpdate);
        }
    }

    @Override
    public AirportResponseDto getAirportByID(String ID) {
        Airport airport = airportRepository.getAirportByID(ID);
        if (airport != null) return new AirportResponseDto(airport);
        return null;
    }

    @Override
    public AirportResponseDto getAirportByName(String name) {
        Airport airport = airportRepository.getAirportByName(name);
        if (airport != null) return new AirportResponseDto(airport);
        return null;
    }

    @Override
    public List<AirportResponseDto> getAllAirports() {
        List<Airport> airports = airportRepository.getAllAirports();
        List<AirportResponseDto> airportResponseDtos = new ArrayList<>();
        if (airports != null) {
            for (Airport airport : airports) {
                airportResponseDtos.add(new AirportResponseDto(airport));
            }
            return airportResponseDtos;
        }
        return null;
    }

    @Override
    public Boolean deleteAirportByID(String ID) {
        Airport airport = airportRepository.getAirportByID(ID);
        return airportRepository.deleteAirportByID(ID, airport.getVersion());
    }
}
