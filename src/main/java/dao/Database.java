package main.java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String connectionString = "jdbc:sqlite:events.db";

    public static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(connectionString);
        return con;
    }
}
