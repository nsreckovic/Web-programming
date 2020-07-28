package com.ns.backend.application.airline.repository;

import com.ns.backend.application.airline.models.Airline;
import com.ns.backend.utils.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AirlineRepositoryImplSQLite implements AirlineRepository {
    private static AirlineRepositoryImplSQLite instance = null;
    private DBConnection dbConnection = DBConnection.getInstance();

    @Override
    public Airline addAirline(Airline airline) {
        String sql = "INSERT INTO Airline(ID, Name, Link, Version) VALUES(?,?,?,?)";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, airline.getID().toString());
            preparedStatement.setString(2, airline.getName());
            preparedStatement.setString(3, airline.getLink());
            preparedStatement.setInt(4, airline.getVersion());
            preparedStatement.executeUpdate();
            System.out.println("Airline: " + airline.getName() + " added successfully.");
            return airline;
        } catch (SQLException e) {
            System.out.println("[addAirline error]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Airline updateAirlineByID(String airline_id, Airline newAirline) {
        String sql = "UPDATE Airline SET Name = ?, Link = ?, Version = ? WHERE ID = ? AND Version = ?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, newAirline.getName());
            preparedStatement.setString(2, newAirline.getLink());
            preparedStatement.setInt(3, newAirline.getVersion());
            preparedStatement.setString(4, airline_id);
            preparedStatement.setInt(5, (newAirline.getVersion() - 1));
            preparedStatement.executeUpdate();
            System.out.println("Airline: " + newAirline.getName() + " updated successfully.");
            return getAirlineByName(newAirline.getName());
        } catch (SQLException e) {
            System.out.println("[updateAirlineByID error]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Airline getAirlineByID(String ID) {
        List<Airline> airlines = selectQuery("SELECT * FROM Airline WHERE ID='" + ID + "' ORDER BY Name");
        if (!airlines.isEmpty()) return airlines.get(0);
        return null;
    }

    @Override
    public Airline getAirlineByName(String name) {
        List<Airline> airlines = selectQuery("SELECT * FROM Airline WHERE Name='" + name + "' ORDER BY Name");
        if (!airlines.isEmpty()) return airlines.get(0);
        return null;
    }

    @Override
    public List<Airline> getAllAirlines() {
        List<Airline> airlines = selectQuery("SELECT * FROM Airline ORDER BY Name");
        if (!airlines.isEmpty()) return airlines;
        return null;
    }

    @Override
    public Boolean deleteAirlineByID(String airline_id, int version) {
        if (getAirlineByID(airline_id) == null || getAirlineByID(airline_id).getVersion() != version) return false;
        String sql = "DELETE FROM Airline WHERE ID = ? AND Version = ?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, airline_id);
            preparedStatement.setInt(2, version);
            preparedStatement.executeUpdate();
            System.out.println("Airline: " + airline_id + " deleted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("[deleteAirlineByID error]: " + e.getMessage());
            return false;
        }
    }

    private List<Airline> selectQuery(String query) {
        List<Airline> airlines = new ArrayList<>();
        try {
            Connection conn = dbConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                Airline airline = new Airline(UUID.fromString(rs.getString(1)),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4));
                airlines.add(airline);
            }
        } catch (SQLException e) {
            System.out.println("[selectQuery error]: " + e.getMessage());
        }
        return airlines;
    }

    public static AirlineRepositoryImplSQLite getInstance() {
        if (instance == null) return new AirlineRepositoryImplSQLite();
        return instance;
    }
}
