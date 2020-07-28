package com.ns.backend.application.user.models.request;

import com.ns.backend.application.user.models.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequestDto {
    private String username;
    private String password1;
    private String password2;
    private UserType type;

    public Boolean checkData() {
        if (username == null || username.equals("")) return false;
        if (password1 == null || password1.equals("")) return false;
        if (password2 == null || password2.equals("")) return false;
        if (type == null) return false;
        return true;
    }

    public Boolean checkPasswords() {
        if (password1.equals(password2)) return true;
        return false;
    }

    public Boolean validatePassword() {
        if (password1.length() >= 6 && password1.matches(".*[0-9].*") && password1.matches(".*[A-Za-z].*")) return true;
        return false;
    }

}
