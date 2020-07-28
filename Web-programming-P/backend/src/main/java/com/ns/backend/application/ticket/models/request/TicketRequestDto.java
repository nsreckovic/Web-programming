package com.ns.backend.application.ticket.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDto {
    private String flight_instance_ID;
    private Integer count;

    public Boolean checkData() {
        if (flight_instance_ID == null || flight_instance_ID.equals("")) return false;
        if (count == null) return false;
        return true;
    }

    public Boolean checkCount() {
        if (count < 1) return false;
        return true;
    }

}
