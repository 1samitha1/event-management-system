package main.java.dao;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

//Interface for event class
public interface EventInterface {
    void setup() throws SQLException;
    void loadEvents(InputStream inputStream) throws SQLException;
    ResultSet getAllEvents() throws SQLException;
}
