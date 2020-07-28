package com.ns.backend.application.airport.repository;

import com.ns.backend.application.airport.models.Airport;
import com.ns.backend.utils.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AirportRepositoryImplSQLite implements AirportRepository {
    private static AirportRepositoryImplSQLite instance = null;
    private DBConnection dbConnection = DBConnection.getInstance();

    @Override
    public Airport addAirport(Airport airport) {
        String sql = "INSERT INTO Airport(ID, Name, Place, Version) VALUES(?,?,?,?)";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, airport.getID());
            preparedStatement.setString(2, airport.getName());
            preparedStatement.setString(3, airport.getPlace());
            preparedStatement.setInt(4, airport.getVersion());
            preparedStatement.executeUpdate();
            System.out.println("Airport: " + airport.getName() + " added successfully.");
            return airport;
        } catch (SQLException e) {
            System.out.println("[addAirport error]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Airport updateAirportByID(String ID, Airport newAirport) {
        String sql = "UPDATE Airport SET ID = ?, Name = ?, Place = ?, Version = ? WHERE ID = ? AND Version = ?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, newAirport.getID());
            preparedStatement.setString(2, newAirport.getName());
            preparedStatement.setString(3, newAirport.getPlace());
            preparedStatement.setInt(4, newAirport.getVersion());
            preparedStatement.setString(5, ID);
            preparedStatement.setInt(6, (newAirport.getVersion() - 1));
            preparedStatement.executeUpdate();
            System.out.println("Airport: " + newAirport.getID() + " updated successfully.");
            return getAirportByID(newAirport.getID());
        } catch (SQLException e) {
            System.out.println("[updateAirportByName error]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Airport getAirportByID(String ID) {
        List<Airport> airports = selectQuery("SELECT * FROM Airport WHERE ID='" + ID + "' ORDER BY Place");
        if (!airports.isEmpty()) return airports.get(0);
        return null;
    }

    @Override
    public Airport getAirportByName(String name) {
        List<Airport> airports = selectQuery("SELECT * FROM Airport WHERE Name='" + name + "' ORDER BY Place");
        if (!airports.isEmpty()) return airports.get(0);
        return null;
    }

    @Override
    public List<Airport> getAllAirports() {
        List<Airport> airports = selectQuery("SELECT * FROM Airport ORDER BY Place");
        if (!airports.isEmpty()) return airports;
        return null;
    }

    @Override
    public Boolean deleteAirportByID(String ID, int version) {
        if (getAirportByID(ID) == null || getAirportByID(ID).getVersion() != version) return false;
        String sql = "DELETE FROM Airport WHERE ID = ? AND Version = ?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, ID);
            preparedStatement.setInt(2, version);
            preparedStatement.executeUpdate();
            System.out.println("Airport: " + ID + " deleted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("[deleteAirportByName error]: " + e.getMessage());
            return false;
        }
    }

    private List<Airport> selectQuery(String query) {
        List<Airport> airports = new ArrayList<>();
        try {
            Connection conn = dbConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                Airport airport = new Airport(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4));
                airports.add(airport);
            }
        } catch (SQLException e) {
            System.out.println("[selectQuery error]: " + e.getMessage());
        }
        return airports;
    }

    public static AirportRepositoryImplSQLite getInstance() {
        if (instance == null) return new AirportRepositoryImplSQLite();
        return instance;
    }

}
