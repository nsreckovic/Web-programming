package application.ticket;

import application.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TicketRepository {

    public synchronized static Ticket addTicket(Ticket t) {
        String sql = "INSERT INTO Ticket('id','one_way','from','to','departure_date','return_date','version') VALUES(?,?,?,?,?,?,?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, t.getId().toString());
            pstmt.setBoolean(2, t.isOne_way());
            pstmt.setString(3, t.getFrom());
            pstmt.setString(4, t.getTo());
            pstmt.setDate(5, new java.sql.Date(t.getDeparture_date().getTime()));
            if (t.getReturn_date() == null) pstmt.setDate(6, null);
            else pstmt.setDate(6, new java.sql.Date(t.getReturn_date().getTime()));
            pstmt.setInt(7, t.getVersion());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("AddTicket error: " + e.getMessage());
        }
        return getTicketByID(t.getId());
    }

    public static Ticket getTicketByID(UUID id) {
        List<Ticket> tickets = selectQueryTicketDB("SELECT * FROM Ticket WHERE id='" + id.toString() + "'");
        if (!tickets.isEmpty()) return tickets.get(0);
        return null;
    }

    private static List<Ticket> selectQueryTicketDB(String query) {
        System.out.println(query);
        List<Ticket> tickets = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                if (rs.getDate(6) == null) {
                    Ticket t = new Ticket(UUID.fromString(rs.getString(1)),
                            rs.getBoolean(2),
                            rs.getString(3),
                            rs.getString(4),
                            new java.util.Date(rs.getDate(5).getTime()),
                            null,
                            rs.getInt(7));
                    tickets.add(t);
                } else {
                    Ticket t = new Ticket(UUID.fromString(rs.getString(1)),
                            rs.getBoolean(2),
                            rs.getString(3),
                            rs.getString(4),
                            new java.util.Date(rs.getDate(5).getTime()),
                            new java.util.Date(rs.getDate(6).getTime()),
                            rs.getInt(7));
                    tickets.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        return tickets;
    }

}
