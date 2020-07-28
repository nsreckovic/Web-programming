package com.ns.backend.application.user.models.response;

import com.ns.backend.application.user.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String jwt;
    private String ID;
    private String username;
    private String type;

    public UserResponseDto(User user) {
        this.ID = user.getID().toString();
        this.username = user.getUsername();
        this.type = user.getType().toString();
    }
}
