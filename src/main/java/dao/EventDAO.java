package main.java.dao;

import main.java.model.EventModel;
import main.java.utils.Notification;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO implements EventDAOInterface {
    private final String TableName = "eventsInfo";

    // Method for setting up the eventsInfo table
    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getInstance().getConnection();
             Statement st = connection.createStatement();) {
            String sql = "CREATE TABLE IF NOT EXISTS "  + TableName +  " (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(10) NOT NULL,\n" +
                    "venue VARCHAR(10) NOT NULL, day VARCHAR(5) NOT NULL, price REAL NOT NULL, tickets_sold INTEGER NOT NULL, total_tickets INTEGER NOT NULL, status VARCHAR(10) )";
            st.executeUpdate(sql);
        }
    }

    // load events data from event.dat file to the database
    @Override
    public void loadEvents(InputStream inputStream) throws SQLException {
        String checkIfExistsQuery = "SELECT COUNT(*) FROM " + TableName + " WHERE name = ? AND venue = ? AND day = ?";
        String insertQuery = "INSERT INTO "  + TableName +  " (name, venue, day, price, tickets_sold, total_tickets, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){
            Connection con = Database.getInstance().getConnection();
            String dataLine;

            while((dataLine = br.readLine()) != null){
                //adding into array by separating with ";"
                String[] parts = dataLine.split(";");
                //verify the length of the parts in data line before set into query statement
                if(parts.length == 6){
                    // check if event is already exists using name, venue and day
                    try (PreparedStatement checkStmt = con.prepareStatement(checkIfExistsQuery)) {
                        checkStmt.setString(1, parts[0]);
                        checkStmt.setString(2, parts[1]);
                        checkStmt.setString(3, parts[2]);
                        ResultSet rs = checkStmt.executeQuery();
                        rs.next();
                        int count = rs.getInt(1);

                        if(count == 0){
                            // if event not exists, inserting it
                            try (PreparedStatement stmt = con.prepareStatement(insertQuery)) {
                                // updating statement with the parts of line
                                stmt.setString(1, parts[0]);
                                stmt.setString(2, parts[1]);
                                stmt.setString(3, parts[2]);
                                stmt.setDouble(4, Double.parseDouble(parts[3]));
                                stmt.setInt(5, Integer.parseInt(parts[4]));
                                stmt.setInt(6, Integer.parseInt(parts[5]));
                                stmt.setString(7, "active");
                                stmt.executeUpdate();
                            }
                        }
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
        Connection con = Database.getInstance().getConnection();
        PreparedStatement st = con.prepareStatement(query);
        return st.executeQuery();
    }

    // retrieve price of the event
    @Override
    public double getPriceForEvent(int eventId) throws SQLException {
        String query = "SELECT price FROM " + TableName + " WHERE id = ?";

        try (Connection con = Database.getInstance().getConnection();
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

    // retrieve event by id and name
    @Override
    public ResultSet getEvent(int eventId, String eventName){
        String query = "SELECT * FROM " + TableName + " WHERE id = ? AND name = ?";

        try (Connection con = Database.getInstance().getConnection();
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

    // retrieve day of the event
    @Override
    public String getDayForEvent(int eventId) {
        String query = "SELECT day FROM " + TableName + " WHERE id = ?";

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setInt(1, eventId);
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getString("day") : null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    // retrieve events by event name
    @Override
    public List<EventModel> getEventsByName(String eventName) {
        String query = "SELECT * FROM " + TableName + " WHERE name = ?";
        List<EventModel> eventListForName = new ArrayList<>();

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query)) {
            st.setString(1, eventName);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                eventListForName.add(new EventModel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("venue"),
                        rs.getString("day"),
                        rs.getDouble("price"),
                        rs.getInt("tickets_sold"),
                        rs.getInt("total_tickets"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return eventListForName;
    }

    // function for update the status of events
    @Override
    public boolean changeEventStatus(int eventId, String status){
        String query = "UPDATE " + TableName + " SET status = ? WHERE id = ?";

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query)){

            st.setString(1, status);
            st.setInt(2, eventId);

            int rowsUpdated = st.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // helper method to check duplicates
    private boolean checkForDuplicates(Connection con, EventModel event) {
        String findDuplicatesQuery = "SELECT 1 FROM " + TableName + " WHERE name = ? AND venue = ? AND day = ? AND price = ? AND total_tickets = ?";

        try (PreparedStatement chk = con.prepareStatement(findDuplicatesQuery)) {

            chk.setString(1, event.getName().get());
            chk.setString(2, event.getVenue().get());
            chk.setString(3, event.getDay().get());
            chk.setDouble(4, event.getPrice().get());
            chk.setInt(4, event.getTotalTickets().get());

            try (ResultSet rs = chk.executeQuery()) {
                if (rs.next()) {
                    // duplicate available
                    System.out.println("Error: Duplicate event found.");
                    return true;
                }else {
                    // no duplicate available
                    return false;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // update selected event
    @Override
    public boolean updateEvent(EventModel event) {
        String updateQuery = "UPDATE " + TableName + " SET name = ?, venue = ?, day = ?, price = ?, total_tickets = ?, status = ?, tickets_sold = ? WHERE id = ?";

        try (Connection con = Database.getInstance().getConnection()) {

            // Check for duplicates
            boolean duplicatedFound = checkForDuplicates(con, event);
            if(duplicatedFound){
                return false;
            }

            PreparedStatement ps = con.prepareStatement(updateQuery);

            ps.setString(1, event.getName().get());
            ps.setString(2, event.getVenue().get());
            ps.setString(3, event.getDay().get());
            ps.setDouble(4, event.getPrice().get());
            ps.setInt(5, event.getTotalTickets().get());
            ps.setString(6, event.getStatus().get());
            ps.setInt(7, event.getTicketsSold().get());
            ps.setInt(8, event.getId().get());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // create a new event
    @Override
    public boolean createNewEvent(EventModel event) {
        String insertQuery = "INSERT INTO " + TableName + " (name, venue, day, price, tickets_sold, total_tickets, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Database.getInstance().getConnection()) {

            // Check for duplicates
            boolean duplicatedFound = checkForDuplicates(con, event);
            if(duplicatedFound){
                return false;
            }

            // if no duplicates, inserting new event
            PreparedStatement ps = con.prepareStatement(insertQuery);

            ps.setString(1, event.getName().get());
            ps.setString(2, event.getVenue().get());
            ps.setString(3, event.getDay().get());
            ps.setDouble(4, event.getPrice().get());
            ps.setInt(5, event.getTicketsSold().get());
            ps.setInt(6, event.getTotalTickets().get());
            ps.setString(7, event.getStatus().get());

            int insertRes = ps.executeUpdate();
            return insertRes > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // delete an event by id
    @Override
    public boolean deleteEvent(int eventId) {
        String query = "DELETE FROM " + TableName + " WHERE id = ?";

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, eventId);
            int deleteRes = ps.executeUpdate();
            return deleteRes > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
