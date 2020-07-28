package com.ns.backend.application.user.models;

import com.ns.backend.application.user.models.request.UserRegisterRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID ID;
    private String username;
    private String password;
    private UserType type;
    private Integer version;

    public User(UserRegisterRequestDto userLoginRequestDto) {
        this.ID = UUID.randomUUID();
        this.username = userLoginRequestDto.getUsername();
        this.password = userLoginRequestDto.getPassword1();
        this.type = userLoginRequestDto.getType();
        this.version = 1;
    }

    public void incrementVersion() { this.version++; }

}
