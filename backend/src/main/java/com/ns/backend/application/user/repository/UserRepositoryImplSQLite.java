package com.ns.backend.application.user.repository;

import com.ns.backend.application.user.models.User;
import com.ns.backend.application.user.models.UserType;
import com.ns.backend.utils.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepositoryImplSQLite implements UserRepository {
    private static UserRepositoryImplSQLite instance = null;
    private DBConnection dbConnection = DBConnection.getInstance();

    @Override
    public User addUser(User user) {
        String sql = "INSERT INTO User(ID, Username, Password, Type, Version) VALUES(?,?,?,?,?)";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.getID().toString());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getType().toString());
            preparedStatement.setInt(5, user.getVersion());
            preparedStatement.executeUpdate();
            System.out.println("User: " + user.getUsername() + " added successfully.");
            return user;
        } catch (SQLException e) {
            System.out.println("[addUser error]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        List<User> users = selectQuery("SELECT * FROM User WHERE Username='" + username + "'");
        if (!users.isEmpty()) return users.get(0);
        return null;
    }

    @Override
    public User updateUserByUsername(String username, User newUser) {
        String sql = "UPDATE User SET Username = ?, Password = ?, Type = ?, Version = ? WHERE Username = ? AND Version = ?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, newUser.getUsername());
            preparedStatement.setString(2, newUser.getPassword());
            preparedStatement.setString(3, newUser.getType().toString());
            preparedStatement.setInt(4, newUser.getVersion());
            preparedStatement.setString(5, username);
            preparedStatement.setInt(6, (newUser.getVersion() - 1));
            preparedStatement.executeUpdate();
            System.out.println("User: " + newUser.getUsername() + " updated successfully.");
            return getUserByUsername(newUser.getUsername());
        } catch (SQLException e) {
            System.out.println("[updateUserByUsername error]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public User getUserByID(String ID) {
        List<User> users = selectQuery("SELECT * FROM User WHERE ID='" + ID + "'");
        if (!users.isEmpty()) return users.get(0);
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = selectQuery("SELECT * FROM User ORDER BY Type");
        if (!users.isEmpty()) return users;
        return null;
    }

    @Override
    public List<User> getUsersByType(UserType type) {
        List<User> users = selectQuery("SELECT * FROM User WHERE Type='" + type + "' ORDER BY Username");
        if (!users.isEmpty()) return users;
        return null;
    }

    @Override
    public Boolean deleteUserByUsername(String username, int version) {
        if (getUserByUsername(username) == null || getUserByUsername(username).getVersion() != version) return false;
        String sql = "DELETE FROM User WHERE Username = ? AND Version = ?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, version);
            preparedStatement.executeUpdate();
            System.out.println("User: " + username + " deleted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("[deleteUserByUsername error]: " + e.getMessage());
            return false;
        }
    }

    private List<User> selectQuery(String query) {
        List<User> users = new ArrayList<>();
        try {
            Connection conn = dbConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                User user = new User(UUID.fromString(rs.getString(1)),
                        rs.getString(2),
                        rs.getString(3),
                        UserType.valueOf(rs.getString(4)),
                        rs.getInt(5));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("[selectQuery error]: " + e.getMessage());
        }
        return users;
    }

    public static UserRepositoryImplSQLite getInstance() {
        if (instance == null) instance = new UserRepositoryImplSQLite();
        return instance;
    }

}
