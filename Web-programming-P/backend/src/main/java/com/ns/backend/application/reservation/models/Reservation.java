package com.ns.backend.application.reservation.models;

import com.ns.backend.application.reservation.models.request.ReservationRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    private UUID ID;
    private String departure_ticket_ID;
    private String return_ticket_ID;
    private String user_ID;
    private Timestamp date;
    private Integer version;

    public Reservation(ReservationRequestDto reservationRequestDto) {
        this.ID = UUID.randomUUID();
        this.departure_ticket_ID = reservationRequestDto.getDeparture_ticket_ID();
        this.return_ticket_ID = reservationRequestDto.getReturn_ticket_ID();
        this.user_ID = reservationRequestDto.getUser_ID();
        this.date = reservationRequestDto.getSqlDate();
        this.version = 1;
    }

    public Reservation(String departure_ticket_ID, String return_ticket_ID, String user_ID) {
        this.ID = UUID.randomUUID();
        this.departure_ticket_ID = departure_ticket_ID;
        this.return_ticket_ID = return_ticket_ID;
        this.user_ID = user_ID;
        this.version = 1;
    }

    public void incrementVersion() {
        this.version++;
    }
}
