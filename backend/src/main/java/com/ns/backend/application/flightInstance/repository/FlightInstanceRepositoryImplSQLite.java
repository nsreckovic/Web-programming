package com.ns.backend.application.flightInstance.repository;

import com.ns.backend.application.flightInstance.models.FlightInstance;
import com.ns.backend.utils.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlightInstanceRepositoryImplSQLite implements FlightInstanceRepository {
    private static FlightInstanceRepositoryImplSQLite instance = null;
    private DBConnection dbConnection = DBConnection.getInstance();

    @Override
    public FlightInstance addFlightInstance(FlightInstance flightInstance) {
        String sql = "INSERT INTO FlightInstance(ID, Flight_ID, Date, Version) VALUES(?,?,?,?)";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, flightInstance.getID());
            preparedStatement.setString(2, flightInstance.getFlight_ID());
            preparedStatement.setDate(3, flightInstance.getDate());
            preparedStatement.setInt(4, flightInstance.getVersion());
            preparedStatement.executeUpdate();
            System.out.println("FlightInstance: " + flightInstance.getID() + " added successfully.");
            return flightInstance;
        } catch (SQLException e) {
            System.out.println("[addFlightInstance error]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public FlightInstance updateFlightInstance(String ID, FlightInstance flightInstance) {
        String sql = "UPDATE FlightInstance SET Flight_ID = ?, Date = ?, Version = ? WHERE ID = ? AND Version = ?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, flightInstance.getFlight_ID());
            preparedStatement.setDate(2, flightInstance.getDate());
            preparedStatement.setInt(3, flightInstance.getVersion());
            preparedStatement.setString(4, ID);
            preparedStatement.setInt(5, (flightInstance.getVersion() - 1));
            preparedStatement.executeUpdate();
            System.out.println("FlightInstance: " + ID + " updated successfully.");
            return getFlightInstanceByFlightAndDate(flightInstance.getFlight_ID(), flightInstance.getDate().getTime());
        } catch (SQLException e) {
            System.out.println("[updateFlightByID error]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public FlightInstance getFlightInstanceByID(String ID) {
        List<FlightInstance> flightInstances = selectQuery("SELECT * FROM FlightInstance WHERE ID='" + ID + "'");
        if (!flightInstances.isEmpty()) return flightInstances.get(0);
        return null;
    }

    @Override
    public FlightInstance getFlightInstanceByFlightAndDate(String flight_ID, Long date) {
        List<FlightInstance> flightInstances = selectQuery("SELECT * FROM FlightInstance WHERE Flight_ID='" + flight_ID + "' AND Date='" + date + "'");
        if (!flightInstances.isEmpty()) return flightInstances.get(0);
        return null;
    }

    @Override
    public List<FlightInstance> getAll() {
        List<FlightInstance> flightInstances = selectQuery("SELECT * FROM FlightInstance ORDER BY Date");
        if (!flightInstances.isEmpty()) return flightInstances;
        return null;
    }

    @Override
    public Boolean deleteFlightInstanceByID(String ID, int version) {
        if (getFlightInstanceByID(ID) == null || getFlightInstanceByID(ID).getVersion() != version) return false;
        String sql = "DELETE FROM FlightInstance WHERE ID = ? AND Version = ?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, ID);
            preparedStatement.setInt(2, version);
            preparedStatement.executeUpdate();
            System.out.println("FlightInstance : " + ID + " deleted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("[deleteFlightInstanceByID error]: " + e.getMessage());
            return false;
        }
    }

    private List<FlightInstance> selectQuery(String query) {
        List<FlightInstance> flightInstances = new ArrayList<>();
        try {
            Connection conn = dbConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                FlightInstance flight = new FlightInstance(rs.getString(1),
                        rs.getString(2),
                        rs.getDate(3),
                        rs.getInt(4));
                flightInstances.add(flight);
            }
        } catch (SQLException e) {
            System.out.println("[selectQuery error]: " + e.getMessage());
        }
        return flightInstances;
    }

    public static FlightInstanceRepositoryImplSQLite getInstance() {
        if (instance == null) return new FlightInstanceRepositoryImplSQLite();
        return instance;
    }

}
