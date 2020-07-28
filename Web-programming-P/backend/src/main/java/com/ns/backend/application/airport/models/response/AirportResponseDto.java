package com.ns.backend.application.airport.models.response;

import com.ns.backend.application.airport.models.Airport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportResponseDto {
    private String ID;
    private String name;
    private String place;

    public AirportResponseDto(Airport airport) {
        this.ID = airport.getID();
        this.name = airport.getName();
        this.place = airport.getPlace();
    }
}
