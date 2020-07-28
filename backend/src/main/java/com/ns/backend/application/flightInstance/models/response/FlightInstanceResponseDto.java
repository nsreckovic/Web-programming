package com.ns.backend.application.flightInstance.models.response;

import com.ns.backend.application.flightInstance.models.FlightInstance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightInstanceResponseDto {
    private String flightInstance_ID;
    private String flight_ID;
    private String date;

    public FlightInstanceResponseDto(FlightInstance flightInstance) {
        flightInstance_ID = flightInstance.getID().toString();
        flight_ID = flightInstance.getFlight_ID();
        date = flightInstance.getDate().toString();
    }
}
