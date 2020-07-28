package com.ns.backend.application.user.models.request;

import com.ns.backend.application.user.models.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDto {
    private String current_username;
    private String new_username;
    private String current_password;
    private String password1;
    private String password2;
    private UserType current_type;
    private UserType new_type;

    public Boolean checkData() {
        if (current_username == null || current_username.equals("")) return false;
        if (new_username == null || new_username.equals("")) return false;
        if (current_password == null || current_password.equals("")) return false;
        if (password1 == null || password1.equals("")) return false;
        if (password2 == null || password2.equals("")) return false;
        if (current_type == null) return false;
        if (new_type == null) return false;
        return true;
    }

    public Boolean checkPasswords() {
        if (password1.equals(password2)) return true;
        return false;
    }

}