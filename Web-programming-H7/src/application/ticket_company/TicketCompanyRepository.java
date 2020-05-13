package application.ticket_company;

import application.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketCompanyRepository {

    public synchronized static TicketCompany addTC(TicketCompany tc) {
        String sql = "INSERT INTO TicketCompany('companyID','ticketID') VALUES(?,?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tc.getCompanyID().toString());
            pstmt.setString(2, tc.getTicketID().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("AddTC error: " + e.getMessage());
        }
        return tc;
    }

    public static List<TicketCompany> getAllTCs() {
        return selectQueryTicketCompanyDB("SELECT * FROM TicketCompany");
    }

    private static List<TicketCompany> selectQueryTicketCompanyDB(String query) {
        System.out.println(query);
        List<TicketCompany> tcs = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                TicketCompany tc = new TicketCompany(UUID.fromString(rs.getString(1)), UUID.fromString(rs.getString(2)));
                tcs.add(tc);
            }
        } catch (SQLException e) {
            System.err.println("Error " + e);
        }
        return tcs;
    }

}
