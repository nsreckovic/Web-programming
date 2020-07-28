package com.ns.backend.application.airline.repository;

import com.ns.backend.application.airline.models.Airline;

import java.util.List;

public interface AirlineRepository {

    Airline addAirline(Airline airline);

    Airline updateAirlineByID(String airliine_id, Airline newAirline);

    Airline getAirlineByID(String ID);

    Airline getAirlineByName(String name);

    List<Airline> getAllAirlines();

    Boolean deleteAirlineByID(String airline_id, int version);

}
