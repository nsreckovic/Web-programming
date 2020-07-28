package com.ns.backend.application.reservation.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {
    private String id;
    private String from;
    private String from_id;
    private String to;
    private String to_id;
    private String departure_date;
    private String flight_id;
    private String airline;
    private String airline_link;
    private String return_date;
    private String return_flight_id;
    private String return_airline;
    private String return_airline_link;
    private String user_id;
    private String username;
    private String from_ticket_id;
    private String return_ticket_id;
    private String reservation_date;
}
