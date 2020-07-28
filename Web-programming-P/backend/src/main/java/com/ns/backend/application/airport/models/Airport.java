package com.ns.backend.application.airport.models;

import com.ns.backend.application.airport.models.request.AirportRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Airport {
    private String ID;
    private String name;
    private String place;
    private Integer version;

    public Airport(AirportRequestDto airportRequestDto) {
        this.ID = airportRequestDto.getId();
        this.name = airportRequestDto.getName();
        this.place = airportRequestDto.getPlace();
        this.version = 1;
    }

    public void incrementVersion() { this.version++; }

}
