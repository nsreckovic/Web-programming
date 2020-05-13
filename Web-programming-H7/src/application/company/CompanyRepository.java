package application.company;

import application.util.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompanyRepository {

    public static List<Company> getCompanies() {
        return selectQueryCompanyDB("SELECT * FROM Company");
    }

    public static Company getCompanyByID(UUID id) {
        List<Company> comps = selectQueryCompanyDB("SELECT * FROM Company WHERE id='" + id.toString() + "'");
        if (!comps.isEmpty()) return comps.get(0);
        return null;
    }

    public static Company getCompanyByName(String name) {
        List<Company> comps = selectQueryCompanyDB("SELECT * FROM Company WHERE name='" + name + "'");
        if (!comps.isEmpty()) return comps.get(0);
        return null;
    }

    private static List<Company> selectQueryCompanyDB(String query) {
        List<Company> comps = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                Company c = new Company(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4));
                comps.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error " + e);
        }
        return comps;
    }

}
