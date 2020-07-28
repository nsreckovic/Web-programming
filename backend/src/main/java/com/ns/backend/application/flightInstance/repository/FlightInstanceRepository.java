package com.ns.backend.application.flightInstance.repository;

import com.ns.backend.application.flightInstance.models.FlightInstance;

import java.util.List;

public interface FlightInstanceRepository {

    FlightInstance addFlightInstance(FlightInstance flightInstance);

    FlightInstance updateFlightInstance(String id, FlightInstance flightInstance);

    FlightInstance getFlightInstanceByID(String ID);

    FlightInstance getFlightInstanceByFlightAndDate(String flight_ID, Long date);

    List<FlightInstance> getAll();

    Boolean deleteFlightInstanceByID(String ID, int version);

}
