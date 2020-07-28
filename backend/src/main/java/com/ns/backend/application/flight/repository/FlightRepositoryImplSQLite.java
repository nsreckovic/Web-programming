package com.ns.backend.application.flight.repository;

import com.ns.backend.application.flight.models.Flight;
import com.ns.backend.utils.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlightRepositoryImplSQLite implements FlightRepository {
    private static FlightRepositoryImplSQLite instance = null;
    private DBConnection dbConnection = DBConnection.getInstance();

    @Override
    public Flight addFlight(Flight flight) {
        String sql = "INSERT INTO Flight(ID, Airline_ID, Departure_airport_ID, Arrival_airport_ID, Version) VALUES(?,?,?,?,?)";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, flight.getID());
            preparedStatement.setString(2, flight.getAirline_ID());
            preparedStatement.setString(3, flight.getDeparture_airport_ID());
            preparedStatement.setString(4, flight.getArrival_airport_ID());
            preparedStatement.setInt(5, flight.getVersion());
            preparedStatement.executeUpdate();
            System.out.println("Flight: " + flight.getID() + " added successfully.");
            return flight;
        } catch (SQLException e) {
            System.out.println("[addFlight error]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Flight updateFlightByID(String ID, Flight newFlight) {
        String sql = "UPDATE Flight SET ID = ?, Airline_ID = ?, Departure_airport_ID = ?, Arrival_airport_ID = ?, Version = ? WHERE ID = ? AND Version = ?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, newFlight.getID());
            preparedStatement.setString(2, newFlight.getAirline_ID());
            preparedStatement.setString(3, newFlight.getDeparture_airport_ID());
            preparedStatement.setString(4, newFlight.getArrival_airport_ID());
            preparedStatement.setInt(5, newFlight.getVersion());
            preparedStatement.setString(6, ID);
            preparedStatement.setInt(7, (newFlight.getVersion() - 1));
            preparedStatement.executeUpdate();
            System.out.println("Flight: " + newFlight.getID() + " updated successfully.");
            return getFlightByID(newFlight.getID());
        } catch (SQLException e) {
            System.out.println("[updateFlightByID error]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Flight getFlightByID(String ID) {
        List<Flight> flights = selectQuery("SELECT * FROM Flight WHERE ID='" + ID + "' ORDER BY ID");
        if (!flights.isEmpty()) return flights.get(0);
        return null;
    }

    @Override
    public Flight getFlightByName(String name) {
        List<Flight> flights = selectQuery("SELECT * FROM Flight WHERE Name='" + name + "' ORDER BY ID");
        if (!flights.isEmpty()) return flights.get(0);
        return null;
    }

    @Override
    public List<Flight> getAllFlights() {
        List<Flight> flights = selectQuery("SELECT * FROM Flight ORDER BY ID");
        if (!flights.isEmpty()) return flights;
        return null;
    }

    @Override
    public List<Flight> getFlightsByDepartureAirportID(String departure_airport_ID) {
        List<Flight> flights = selectQuery("SELECT * FROM Flight WHERE Departure_airport_ID='" + departure_airport_ID + "' ORDER BY ID");
        if (!flights.isEmpty()) return flights;
        return null;
    }

    @Override
    public List<Flight> getFlightsByArrivalAirportID(String arrival_airport_ID) {
        List<Flight> flights = selectQuery("SELECT * FROM Flight WHERE Arrival_airport_ID='" + arrival_airport_ID + "' ORDER BY ID");
        if (!flights.isEmpty()) return flights;
        return null;
    }

    @Override
    public Boolean deleteFlightByID(String ID, int version) {
        if (getFlightByID(ID) == null || getFlightByID(ID).getVersion() != version) return false;
        String sql = "DELETE FROM Flight WHERE ID = ? AND Version = ?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, ID);
            preparedStatement.setInt(2, version);
            preparedStatement.executeUpdate();
            System.out.println("Flight : " + ID + " deleted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("[deleteFlightByID error]: " + e.getMessage());
            return false;
        }
    }

    private List<Flight> selectQuery(String query) {
        List<Flight> flights = new ArrayList<>();
        try {
            Connection conn = dbConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                Flight flight = new Flight(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5));
                flights.add(flight);
            }
        } catch (SQLException e) {
            System.out.println("[selectQuery error]: " + e.getMessage());
        }
        return flights;
    }

    public static FlightRepositoryImplSQLite getInstance() {
        if (instance == null) return new FlightRepositoryImplSQLite();
        return instance;
    }
}
