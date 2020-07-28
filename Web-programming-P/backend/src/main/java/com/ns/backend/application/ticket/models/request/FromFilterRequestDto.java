package com.ns.backend.application.ticket.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FromFilterRequestDto {
    private String from_date_filter;
    private String to_date_filter;
    private String from_filter;
    private String to_filter;
    private String airline;
    private String current_departure_ticket_id;

    public Boolean allNull() {
        if ((from_date_filter == null || from_date_filter.equals("")) &&
            (to_date_filter == null || to_date_filter.equals("")) &&
            (from_filter == null || from_filter.equals("")) &&
            (to_filter == null || to_filter.equals("")) &&
            (airline == null || airline.equals("")) &&
            (current_departure_ticket_id == null || current_departure_ticket_id.equals(""))) return true;
        return false;
    }
}
