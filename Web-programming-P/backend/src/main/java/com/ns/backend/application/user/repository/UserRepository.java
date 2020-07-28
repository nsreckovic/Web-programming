package com.ns.backend.application.user.repository;

import com.ns.backend.application.user.models.User;
import com.ns.backend.application.user.models.UserType;

import java.util.List;

public interface UserRepository {

    User addUser(User user);

    User getUserByUsername(String username);

    User updateUserByUsername(String username, User newUser);

    User getUserByID(String ID);

    List<User> getAllUsers();

    List<User> getUsersByType(UserType type);

    Boolean deleteUserByUsername(String username, int version);

}
