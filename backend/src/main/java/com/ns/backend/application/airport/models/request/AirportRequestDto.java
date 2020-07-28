package com.ns.backend.application.airport.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportRequestDto {
    private String id;
    private String name;
    private String place;

    public Boolean checkData() {
        if (id == null || id.equals("")) return false;
        if (name == null || name.equals("")) return false;
        if (place == null || place.equals("")) return false;
        return true;
    }
}
