package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseDAO {

    public Connection getCon() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecofoods", "root", "admin");
            System.out.println("Database connected");
        } catch (ClassNotFoundException e) {
            Logger.getLogger(BaseDAO.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("MySQL JDBC Driver not found");
        } catch (SQLException e) {
            Logger.getLogger(BaseDAO.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
        return con;
    }
}
