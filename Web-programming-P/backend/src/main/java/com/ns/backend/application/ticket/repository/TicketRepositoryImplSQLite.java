package com.ns.backend.application.ticket.repository;

import com.ns.backend.application.ticket.models.Ticket;
import com.ns.backend.utils.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketRepositoryImplSQLite implements TicketRepository {
    private static TicketRepositoryImplSQLite instance = null;
    private DBConnection dbConnection = DBConnection.getInstance();

    @Override
    public Ticket addTicket(Ticket ticket) {
        String sql = "INSERT INTO Ticket(ID, Flight_instance_ID, Ticket_Count, Version) VALUES(?,?,?,?)";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, ticket.getID().toString());
            preparedStatement.setString(2, ticket.getFlight_instance_ID());
            preparedStatement.setInt(3, ticket.getCount());
            preparedStatement.setInt(4, ticket.getVersion());
            preparedStatement.executeUpdate();
            System.out.println("Ticket: " + ticket.getID() + " added successfully.");
            return ticket;
        } catch (SQLException e) {
            System.out.println("[addTicket error]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Ticket updateTicketByID(String ID, Ticket newTicket) {
        String sql = "UPDATE Ticket SET Flight_instance_ID = ?, Ticket_Count = ?, Version = ? WHERE ID = ? AND Version = ?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, newTicket.getFlight_instance_ID());
            System.out.println(newTicket.getCount());
            preparedStatement.setInt(2, newTicket.getCount());
            preparedStatement.setInt(3, newTicket.getVersion());
            preparedStatement.setString(4, ID);
            preparedStatement.setInt(5, (newTicket.getVersion() - 1));
            preparedStatement.executeUpdate();
            System.out.println("Ticket: " + newTicket.getID() + " updated successfully.");
            return getTicketByID(newTicket.getID().toString());
        } catch (SQLException e) {
            System.out.println("[updateTicketByID error]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Ticket getTicketByID(String ID) {
        List<Ticket> tickets = selectQuery("SELECT * FROM Ticket WHERE ID='" + ID + "' ORDER BY Flight_instance_ID");
        if (!tickets.isEmpty()) return tickets.get(0);
        return null;
    }

    @Override
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = selectQuery("SELECT Ticket.ID, Ticket.Flight_instance_ID, Ticket.Ticket_Count, Ticket.Version from Ticket\n" +
                "JOIN FlightInstance\n" +
                "ON FlightInstance.ID = Ticket.Flight_instance_ID\n" +
                "ORDER BY FlightInstance.Date");
        if (!tickets.isEmpty()) return tickets;
        return null;
    }

    @Override
    public List<Ticket> getTicketsForUser(String user_id) {
        List<Ticket> tickets = selectQuery("SELECT Ticket.ID, Ticket.Flight_instance_ID, Ticket.Ticket_Count, Ticket.Version from Reservation\n" +
                "JOIN Ticket\n" +
                "ON (Reservation.Departure_ticket_ID = Ticket.ID OR Reservation.Return_ticket_ID = Ticket.ID)\n" +
                "JOIN FlightInstance\n" +
                "ON FlightInstance.ID = Ticket.Flight_instance_ID\n" +
                "WHERE Reservation.User_ID = '" + user_id + "'\n" +
                "ORDER BY FlightInstance.Date");
        if (!tickets.isEmpty()) return tickets;
        return null;
    }

    @Override
    public List<Ticket> getAllTicketsByAirline(String airline_id) {
        List<Ticket> tickets = selectQuery("SELECT Ticket.ID, Ticket.Flight_instance_ID, Ticket.Ticket_Count, Ticket.Version from Ticket\n" +
                "JOIN FlightInstance\n" +
                "ON Ticket.Flight_instance_ID = FlightInstance.ID\n" +
                "JOIN Flight\n" +
                "ON FlightInstance.Flight_ID = Flight.ID\n" +
                "WHERE Flight.Airline_ID = '" + airline_id + "'");
        if (!tickets.isEmpty()) return tickets;
        return null;
    }

    @Override
    public List<Ticket> getAllAvailableTickets() {
        List<Ticket> tickets = selectQuery("SELECT * FROM Ticket WHERE Ticket_Count > 0 ORDER BY Flight_instance_ID");
        if (!tickets.isEmpty()) return tickets;
        return null;
    }

    /*
        Returns all tickets that have the same departure airport as arrival airport of the given one,
        with flight instances after the given one.
     */
    @Override
    public List<Ticket> getReturnTicketsByID(String ID) {
        String sql = "SELECT Ticket.ID, Ticket.Flight_instance_ID, Ticket.Ticket_Count, Ticket.Version from Ticket\n" +
                "JOIN FlightInstance \n" +
                "ON Ticket.Flight_instance_ID = FlightInstance.ID \n" +
                "JOIN Flight \n" +
                "ON FlightInstance.Flight_ID = Flight.ID\n" +
                "JOIN Airport \n" +
                "ON Flight.Departure_airport_ID = Airport.ID\n" +
                "WHERE Flight.Arrival_airport_ID = \n" +
                "(SELECT Airport.ID from Ticket\n" +
                "JOIN FlightInstance \n" +
                "ON Ticket.Flight_instance_ID = FlightInstance.ID \n" +
                "JOIN Flight \n" +
                "ON FlightInstance.Flight_ID = Flight.ID\n" +
                "JOIN Airport \n" +
                "ON Flight.Departure_airport_ID = Airport.ID\n" +
                "WHERE Ticket.ID = '" + ID + "')\n" +
                "AND FlightInstance.Date >= \n" +
                "(SELECT FlightInstance.Date from Ticket\n" +
                "JOIN FlightInstance \n" +
                "ON Ticket.Flight_instance_ID = FlightInstance.ID \n" +
                "WHERE Ticket.ID = '" + ID + "')\n" +
                "AND Ticket.Ticket_Count > 0\n" +
                "ORDER BY Flight_instance_ID";
        List<Ticket> tickets = selectQuery(sql);
        if (!tickets.isEmpty()) return tickets;
        return null;
    }

    @Override
    public Boolean deleteTicketByID(String ID, int version) {
        if (getTicketByID(ID) == null || getTicketByID(ID).getVersion() != version) return false;
        String sql = "DELETE FROM Ticket WHERE ID = ? AND Version = ?";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, ID);
            preparedStatement.setInt(2, version);
            preparedStatement.executeUpdate();
            System.out.println("Ticket : " + ID + " deleted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("[deleteTicketByID error]: " + e.getMessage());
            return false;
        }
    }

    private List<Ticket> selectQuery(String query) {
        List<Ticket> tickets = new ArrayList<>();
        try {
            Connection conn = dbConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                Ticket ticket = new Ticket(UUID.fromString(rs.getString(1)),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getInt(4));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.out.println("[selectQuery error]: " + e.getMessage());
        }
        return tickets;
    }

    public static TicketRepositoryImplSQLite getInstance() {
        if (instance == null) return new TicketRepositoryImplSQLite();
        return instance;
    }
}
