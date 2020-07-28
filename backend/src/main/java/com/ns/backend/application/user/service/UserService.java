package com.ns.backend.application.user.service;

import com.ns.backend.application.user.models.UserType;
import com.ns.backend.application.user.models.request.UserLoginRequestDto;
import com.ns.backend.application.user.models.request.UserRegisterRequestDto;
import com.ns.backend.application.user.models.response.UserResponseDto;

import java.util.List;

public interface UserService {

    Boolean isUsernameAvailable(String username);

    UserResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto);

    UserResponseDto loginUser(UserLoginRequestDto userLoginRequestDto);

    UserResponseDto updateUser(String current_username, UserRegisterRequestDto newUser);

    UserResponseDto getUserByUsername(String username);

    List<UserResponseDto> getAllUsers();

    List<UserResponseDto> getUsersByType(UserType type);

    Boolean deleteUserByUsername(String username);

}
