package com.ns.backend.application.reservation.repository;

import com.ns.backend.application.reservation.models.Reservation;
import com.ns.backend.utils.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservationRepositoryImplSQLite implements ReservationRepository {
    private static ReservationRepositoryImplSQLite instance = null;
    private DBConnection dbConnection = DBConnection.getInstance();

    @Override
    public Reservation addReservation(Reservation reservation) {
        String sql = "INSERT INTO Reservation(ID, Departure_ticket_ID, Return_ticket_ID, User_ID, Reservation_date, Version) VALUES(?,?,?,?,?,?)";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, reservation.getID().toString());
            preparedStatement.setString(2, reservation.getDeparture_ticket_ID());
            preparedStatement.setString(3, reservation.getReturn_ticket_ID());
            preparedStatement.setString(4, reservation.getUser_ID());
            preparedStatement.setTimestamp(5, reservation.getDate());
            preparedStatement.setInt(6, reservation.getVersion());
            preparedStatement.executeUpdate();
            System.out.println("Reservation: " + reservation.getID().toString() + " added successfully.");
            return reservation;
        } catch (SQLException e) {
            System.out.println("[addReservation error]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Reservation updateReservationByID(String ID, Reservation newReservation) {
        String sql = "UPDATE Reservation SET Departure_ticket_ID = ?, Return_ticket_ID = ?, User_ID = ?, Reservation_date = ?, Version = ? WHERE ID = ? AND Version = ?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, newReservation.getDeparture_ticket_ID());
            preparedStatement.setString(2, newReservation.getReturn_ticket_ID());
            preparedStatement.setString(3, newReservation.getUser_ID());
            preparedStatement.setTimestamp(4, newReservation.getDate());
            preparedStatement.setInt(5, newReservation.getVersion());
            preparedStatement.setString(6, ID);
            preparedStatement.setInt(7, (newReservation.getVersion() - 1));
            preparedStatement.executeUpdate();
            System.out.println("Reservation: " + newReservation.getID() + " updated successfully.");
            return getReservationByID(newReservation.getID().toString());
        } catch (SQLException e) {
            System.out.println("[updateReservationByID error]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Reservation getReservationByID(String ID) {
        List<Reservation> reservations = selectQuery("SELECT * FROM Reservation WHERE ID='" + ID + "' ORDER BY User_ID");
        if (!reservations.isEmpty()) return reservations.get(0);
        return null;
    }

    @Override
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = selectQuery("SELECT * FROM Reservation ORDER BY User_ID");
        if (!reservations.isEmpty()) return reservations;
        return null;
    }

    @Override
    public List<Reservation> getAllReservationsByUserID(String user_ID) {
        List<Reservation> reservations = selectQuery("SELECT * FROM Reservation WHERE User_ID='" + user_ID + "' ORDER BY Reservation_date");
        if (!reservations.isEmpty()) return reservations;
        return null;
    }

    @Override
    public Boolean deleteReservationByID(String ID, int version) {
        if (getReservationByID(ID) == null || getReservationByID(ID).getVersion() != version) return false;
        String sql = "DELETE FROM Reservation WHERE ID = ? AND Version = ?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, ID);
            preparedStatement.setInt(2, version);
            preparedStatement.executeUpdate();
            System.out.println("Reservation : " + ID + " deleted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("[deleteReservationByID error]: " + e.getMessage());
            return false;
        }
    }

    private List<Reservation> selectQuery(String query) {
        List<Reservation> reservations = new ArrayList<>();
        try {
            Connection conn = dbConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                Reservation reservation = new Reservation(UUID.fromString(rs.getString(1)),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getTimestamp(5),
                        rs.getInt(6));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.out.println("[selectQuery error]: " + e.getMessage());
        }
        return reservations;
    }

    public static ReservationRepositoryImplSQLite getInstance() {
        if (instance == null) return new ReservationRepositoryImplSQLite();
        return instance;
    }
}
