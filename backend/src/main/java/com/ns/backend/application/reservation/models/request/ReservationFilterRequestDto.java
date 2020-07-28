package com.ns.backend.application.reservation.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationFilterRequestDto {
    private String from_date_filter;
    private String to_date_filter;
    private String from_filter;
    private String to_filter;
    private String airline;
    private String type;
    private String username;

    public Boolean allNull() {
        if ((this.from_date_filter == null || this.from_date_filter.equals("")) &&
            (this.to_date_filter == null || this.to_date_filter.equals("")) &&
            (this.from_filter == null || this.from_filter.equals("")) &&
            (this.to_filter == null || this.to_filter.equals("")) &&
            (this.airline == null || this.airline.equals("")) &&
            (this.type == null || this.type.equals(""))) return true;
        return false;
    }
}
