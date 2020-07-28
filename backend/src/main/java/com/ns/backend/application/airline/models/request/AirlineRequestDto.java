package com.ns.backend.application.airline.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirlineRequestDto {
    private String name;
    private String link;

    public Boolean checkData() {
        if (this.name == null || this.name.equals("")) return false;
        if (this.link == null || this.link.equals("")) return false;
        return true;
    }
}
