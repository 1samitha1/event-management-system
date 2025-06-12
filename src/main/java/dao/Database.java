package main.java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String connectionString = "jdbc:sqlite:events.db";
    private static Database instance;
    private Connection connection;

    private Database() {
        try {
            this.connection = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database!");
            throw new RuntimeException("Database connection failed", e);
        }
    }

    // use instance of the database connection
    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // get database connection
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(connectionString);
            }
        } catch (SQLException e) {
            System.err.println("Error in : "+e);
        }
        return connection;
    }
}
