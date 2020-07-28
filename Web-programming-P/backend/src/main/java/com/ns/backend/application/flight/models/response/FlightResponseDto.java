package com.ns.backend.application.flight.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponseDto {
    private String flight_ID;
    private String airline;
    private String airline_link;
    private String departure_airport;
    private String arrival_airport;
}
