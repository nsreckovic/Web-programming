package com.ns.backend.application.airport.repository;

import com.ns.backend.application.airport.models.Airport;

import java.util.List;

public interface AirportRepository {

    Airport addAirport(Airport airport);

    Airport updateAirportByID(String ID, Airport newAirport);

    Airport getAirportByID(String ID);

    Airport getAirportByName(String name);

    List<Airport> getAllAirports();

    Boolean deleteAirportByID(String ID, int version);

}
