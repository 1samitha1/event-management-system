package main.java.dao;

import main.java.model.EventModel;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//Interface for event class
public interface EventDAOInterface {
    void setup() throws SQLException;
    void loadEvents(InputStream inputStream) throws SQLException;
    ResultSet getAllEvents() throws SQLException;
    double getPriceForEvent(int eventId) throws SQLException;
    ResultSet getEvent(int eventId, String eventName) throws SQLException;
    String getDayForEvent(int eventId) throws SQLException;
    List<EventModel> getEventsByName(String eventName) throws SQLException;
    boolean changeEventStatus(int eventId, String status) throws SQLException;
}
