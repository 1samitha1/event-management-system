package main.java.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseModel {
    static String connectionString = "jdbc:sqlite:events.db";

    public static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(connectionString);
        return con;
    }

//    private static DatabaseModel instance;
//    private static final String connectionString = "jdbc:sqlite:events.db";
//    private Connection connection;
//
//    // Private constructor so it can't be called from outside
//    private DatabaseModel() {
//        try {
//            connection = DriverManager.getConnection(connectionString);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Synchronized method to ensure thread safety
//    public static synchronized DatabaseModel getInstance() {
//        if (instance == null) {
//            instance = new DatabaseModel();
//        }
//        return instance;
//    }
//
//    // Method to get the single shared connection
//    public Connection getConnection() {
//        return connection;
//    }
}
