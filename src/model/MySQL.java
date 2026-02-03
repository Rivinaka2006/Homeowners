package model;

import java.sql. Connection;
import java.sql. DriverManager;
import java.sql. ResultSet;

public class MySQL {
    
   private static Connection connection;
    
    private static void createConnection() throws Exception {
        if (connection == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/homeowners", "root", "@0788516973wmpJ");
        }
    }
    
    public static ResultSet executeSearch(String query) throws Exception {
        createConnection();
        return connection.createStatement().executeQuery(query);
    }
    public static Integer executeIUD(String query) throws Exception {
        createConnection();
        return connection.createStatement().executeUpdate(query);
    }
    
}
