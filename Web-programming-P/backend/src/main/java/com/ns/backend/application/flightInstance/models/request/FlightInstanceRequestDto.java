package com.ns.backend.application.flightInstance.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightInstanceRequestDto {
    private String flight_ID;
    private String date;

    public boolean checkData() {
        if (flight_ID == null || flight_ID.equals("")) return false;
        if (date == null || date.equals("")) return false;
        return true;
    }

}
