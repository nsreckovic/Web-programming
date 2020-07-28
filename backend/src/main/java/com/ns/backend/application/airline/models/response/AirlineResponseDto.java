package com.ns.backend.application.airline.models.response;

import com.ns.backend.application.airline.models.Airline;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirlineResponseDto {
    private String ID;
    private String name;
    private String link;

    public AirlineResponseDto(Airline airline) {
        this.ID = airline.getID().toString();
        this.name = airline.getName();
        this.link = airline.getLink();
    }
}
