package com.ns.backend.application.flightInstance.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightInstance {
    private String ID;
    private String flight_ID;
    private Date date;
    private Integer version;

    public FlightInstance(String flight_ID, Date date) {
        this.ID = flight_ID + "-" + date.toString();
        this.flight_ID = flight_ID;
        this.date = date;
        version = 1;
    }

    public void incrementVersion() {
        this.version++;
    }
}
