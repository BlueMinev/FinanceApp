package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;



    public Connection getConnection() {
        try {
            String connectionString = String.format("jdbc:sqlite:%s/%s", "src/java/database", "db");
            Class.forName("org.sqlite.JDBC");
            Connection dbConnection = DriverManager.getConnection(connectionString);
            return dbConnection;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
}
