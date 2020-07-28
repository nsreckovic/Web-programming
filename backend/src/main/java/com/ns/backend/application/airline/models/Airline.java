package com.ns.backend.application.airline.models;

import com.ns.backend.application.airline.models.request.AirlineRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Airline {
    private UUID ID;
    private String name;
    private String link;
    private Integer version;

    public Airline(AirlineRequestDto airlineRequestDto) {
        this.ID = UUID.randomUUID();
        this.name = airlineRequestDto.getName();
        this.link = airlineRequestDto.getLink();
        this.version = 1;
    }

    public void incrementVersion() { this.version++; }

}
