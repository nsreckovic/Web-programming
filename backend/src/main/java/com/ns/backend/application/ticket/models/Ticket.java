package com.ns.backend.application.ticket.models;

import com.ns.backend.application.ticket.models.request.TicketRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private UUID ID;
    private String flight_instance_ID;
    private Integer count;
    private Integer version;

    public Ticket(TicketRequestDto ticketRequestDto) {
        this.ID = UUID.randomUUID();
        this.flight_instance_ID = ticketRequestDto.getFlight_instance_ID();
        this.count = ticketRequestDto.getCount();
        this.version = 1;
    }

    public void incrementVersion() { this.version++; }

    public void incrementCount() { this.count++; }

    public Boolean decrementCount() {
        if (this.count > 0) {
            this.count--;
            return true;
        }
        return false;
    }
}
