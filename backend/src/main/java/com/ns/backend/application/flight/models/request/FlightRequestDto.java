package com.ns.backend.application.flight.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightRequestDto {
    private String id;
    private String airline;
    private String departure_airport_ID;
    private String arrival_airport_ID;

    public Boolean checkData() {
        if (id == null || id.equals("")) return false;
        if (airline == null || airline.equals("")) return false;
        if (departure_airport_ID == null || departure_airport_ID.equals("")) return false;
        if (arrival_airport_ID == null || arrival_airport_ID.equals("")) return false;
        return true;
    }
}
