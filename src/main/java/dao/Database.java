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

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

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



//    private Database() {
//        try {
//            connection = Database.getConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static Database getInstance(){
//        if(instance == null){
//            instance = new Database();
//        }
//        return instance;
//    }

//    public static Connection getConnection() throws SQLException {
//        // DriverManager is the basic service for managing a set of JDBC drivers
//        // Can also pass username and password
//        return DriverManager.getConnection(connectionString);
//    }


}
