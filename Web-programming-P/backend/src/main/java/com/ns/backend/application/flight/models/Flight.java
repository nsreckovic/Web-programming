package com.ns.backend.application.flight.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
    private String ID;
    private String airline_ID;
    private String departure_airport_ID;
    private String arrival_airport_ID;
    private Integer version;

    public Flight(String ID, String airline_ID, String departure_airport_ID, String arrival_airport_ID) {
        this.ID = ID;
        this.airline_ID = airline_ID;
        this.departure_airport_ID = departure_airport_ID;
        this.arrival_airport_ID = arrival_airport_ID;
        this.version = 1;
    }

    public void incrementVersion() { this.version++; }
}
