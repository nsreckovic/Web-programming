package com.ns.backend.application.user.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestDto {
    private String username;
    private String password;

    public Boolean checkData() {
        if (username == null || username.equals("")) return false;
        if (password == null || password.equals("")) return false;
        return true;
    }
}
