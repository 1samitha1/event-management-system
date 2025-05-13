package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    String connectionString = "jdbc:sqlite:events.db";

    public Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(connectionString);
        return con;
    }
}
