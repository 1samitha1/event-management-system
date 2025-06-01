package main.java.dao;

import main.java.utils.Notification;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

public class EventDAO implements EventDAOInterface {
    private final String TableName = "eventsInfo";

    // Method for setting up the eventsInfo table
    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement st = connection.createStatement();) {
            String sql = "CREATE TABLE IF NOT EXISTS "  + TableName +  " (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(10) NOT NULL,\n" +
                    "venue VARCHAR(10) NOT NULL, day VARCHAR(5) NOT NULL, price REAL NOT NULL, tickets_sold INTEGER NOT NULL, total_tickets INTEGER NOT NULL )";
            st.executeUpdate(sql);
        }
    }

    // load events data from event.dat file to the database
    @Override
    public void loadEvents(InputStream inputStream) throws SQLException {
        String insertQuery = "INSERT INTO "  + TableName +  " (name, venue, day, price, tickets_sold, total_tickets) VALUES (?, ?, ?, ?, ?, ?)";

        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){
            Connection con = Database.getConnection();
            String dataLine;

            while((dataLine = br.readLine()) != null){
                //adding into array by separating with ";"
                String[] parts = dataLine.split(";");
                //verify the length of the parts in data line before set into query statement
                if(parts.length == 6){
                    try (PreparedStatement stmt = con.prepareStatement(insertQuery)) {
                        // updating statement with the parts of line
                        stmt.setString(1, parts[0]);
                        stmt.setString(2, parts[1]);
                        stmt.setString(3, parts[2]);
                        stmt.setDouble(4, Double.parseDouble(parts[3]));
                        stmt.setInt(5, Integer.parseInt(parts[4]));
                        stmt.setInt(6, Integer.parseInt(parts[5]));
                        stmt.executeUpdate();
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Error while read: "+ e);
            Notification.showError("Error", "Error in reading file!");
        }
    }

    // get events data from database
    @Override
    public ResultSet getAllEvents() throws SQLException {
        String query = "SELECT * FROM " + TableName;
        Connection con = Database.getConnection();
        PreparedStatement st = con.prepareStatement(query);
        return st.executeQuery();
    }

    @Override
    public double getPriceForEvent(int eventId) throws SQLException {
        String query = "SELECT price FROM " + TableName + " WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, eventId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("price");
                } else {
                    throw new SQLException("Event not found for ID: " + eventId);
                }
            }
        }
    }

    @Override
    public ResultSet getEvent(int eventId, String eventName){
        String query = "SELECT * FROM " + TableName + " WHERE id = ? AND name = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, eventId);
            ps.setString(2, eventName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs;
                } else {
                    throw new SQLException("Event not found for ID: " + eventId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    };

    @Override
    public String getDayForEvent(int eventId) {
        String query = "SELECT day FROM " + TableName + " WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setInt(1, eventId);
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getString("day") : null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };
}
