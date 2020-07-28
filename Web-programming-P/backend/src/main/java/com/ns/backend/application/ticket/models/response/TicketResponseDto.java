package com.ns.backend.application.ticket.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDto {
    private String ticket_ID;
    private String flight_instance_ID;
    private Integer count;

    private String flight_ID;

    private String departure_place;
    private String departure_airport_name;
    private String departure_airport_ID;

    private String arrival_place;
    private String arrival_airport_name;
    private String arrival_airport_ID;

    private String departure_date;

    private String airline;
    private String airline_link;
}
