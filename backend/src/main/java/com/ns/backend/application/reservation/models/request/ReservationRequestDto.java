package com.ns.backend.application.reservation.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {
    private String departure_ticket_ID;
    private String return_ticket_ID;
    private String user_ID;
    private String date;
    private Timestamp sqlDate;

    public Boolean checkData() {
        if (this.departure_ticket_ID == null || this.departure_ticket_ID.equals("")) return false;
        if (this.user_ID == null || this.user_ID.equals("")) return false;
        if (this.date == null || this.date.equals("")) return false;
        System.out.println(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date utilDate;
        try { utilDate = sdf.parse(date); }
        catch (ParseException e) { return false; }
        sqlDate = new Timestamp(utilDate.getTime());
        return true;
    }
}
