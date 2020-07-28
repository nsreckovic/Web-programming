package com.ns.backend.application.user.service;

import com.ns.backend.application.user.models.User;
import com.ns.backend.application.user.models.UserType;
import com.ns.backend.application.user.models.request.UserLoginRequestDto;
import com.ns.backend.application.user.models.request.UserRegisterRequestDto;
import com.ns.backend.application.user.models.response.UserResponseDto;
import com.ns.backend.application.user.repository.UserRepository;
import com.ns.backend.application.user.repository.UserRepositoryImplSQLite;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl() {
        userRepository = UserRepositoryImplSQLite.getInstance();
    }

    @Override
    public Boolean isUsernameAvailable(String username) {
        if (userRepository.getUserByUsername(username) == null) return true;
        return false;
    }

    @Override
    public UserResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        User user = userRepository.addUser(new User(userRegisterRequestDto));
        if (user != null) return new UserResponseDto(user);
        return null;
    }

    @Override
    public UserResponseDto loginUser(UserLoginRequestDto userLoginRequestDto) {
        if (!userLoginRequestDto.checkData()) return null;
        User user = userRepository.getUserByUsername(userLoginRequestDto.getUsername());
        if (user != null && userLoginRequestDto.getPassword().equals(user.getPassword())) return new UserResponseDto(user);
        return null;
    }

    @Override
    public UserResponseDto updateUser(String current_username, UserRegisterRequestDto newUser) {
        User userToUpdate = userRepository.getUserByUsername(current_username);
        if (userToUpdate == null) return null;
        userToUpdate.setUsername(newUser.getUsername());
        userToUpdate.setPassword(newUser.getPassword1());
        userToUpdate.setType(newUser.getType());
        userToUpdate.incrementVersion();

        User updatedUser = userRepository.updateUserByUsername(current_username, userToUpdate);
        if (updatedUser != null) return new UserResponseDto(updatedUser);
        return null;
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.getUserByUsername(username);
        if (user != null) return new UserResponseDto(user);
        return null;
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.getAllUsers();
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        if (users != null) {
            for (User user : users) {
                userResponseDtos.add(new UserResponseDto(user));
            }
            return userResponseDtos;
        }
        return null;
    }

    @Override
    public List<UserResponseDto> getUsersByType(UserType type) {
        List<User> users = userRepository.getUsersByType(type);
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        if (users != null) {
            for (User user : users) {
                userResponseDtos.add(new UserResponseDto(user));
            }
            return userResponseDtos;
        }
        return null;
    }

    @Override
    public Boolean deleteUserByUsername(String username) {
        User user = userRepository.getUserByUsername(username);
        return userRepository.deleteUserByUsername(username, user.getVersion());
    }

}
