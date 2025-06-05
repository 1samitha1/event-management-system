package main.java.dao;

import main.java.model.OrderModel;
import main.java.utils.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements OrderDAOInterface {
    private final String TableName = "orders";
    private final String EventTableName = "eventsInfo";
    private final String CartItemsTableName = "cartItems";

    @Override
    public void setup() throws SQLException {
        // creating the orders table if not exists
        try (Connection connection = Database.getInstance().getConnection();
             Statement st = connection.createStatement();) {
            String sql = "CREATE TABLE IF NOT EXISTS "  + TableName +  " (id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR(10) NOT NULL,\n" +
                    "eventId INTEGER NOT NULL, eventName VARCHAR(30) NOT NULL, totalPrice REAL NOT NULL, quantity INTEGER NOT NULL, orderDate VARCHAR(10) NOT NULL, FOREIGN KEY (eventId) REFERENCES eventsInfo(id) )";
            st.executeUpdate(sql);
        }
    }

    // add a new order, update sold tickets count, delete cart items
    @Override
    public boolean addOrder(OrderModel order)  {
        String insertOrderQuery = "INSERT INTO " + TableName + " (username, eventId, eventName, quantity, totalPrice, orderDate) VALUES (?, ?, ?, ?, ?, ?)";
        String updateEventQuery = "UPDATE " + EventTableName + " SET tickets_sold = tickets_sold + ? WHERE id = ?";
        String deleteCartItemQuery = "DELETE FROM " + CartItemsTableName + " WHERE username = ? AND eventId = ?";

        try (Connection con = Database.getInstance().getConnection()) {
            // stop auto commit with executeUpdate;
            con.setAutoCommit(false);

            // insert to order table
            try (PreparedStatement ins = con.prepareStatement(insertOrderQuery)) {
                ins.setString(1, order.getUsername());
                ins.setInt   (2, order.getEventId());
                ins.setString(3, order.getEventName());
                ins.setInt   (4, order.getQuantity());
                ins.setDouble(5, order.getTotalPrice());
                ins.setString(6, order.getOrderDate());
                ins.executeUpdate();
            }

            // update sold_tickets in eventsInfo table
            try (PreparedStatement up = con.prepareStatement(updateEventQuery)) {
                up.setInt(1, order.getQuantity());
                up.setInt(2, order.getEventId());
                up.executeUpdate();
            }

            // delete cart item from cartItem table
            try (PreparedStatement del = con.prepareStatement(deleteCartItemQuery)) {
                del.setString(1, order.getUsername());
                del.setInt(2, order.getEventId());
                del.executeUpdate();
            }

            // commit the database query
            con.commit();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // retrieve orders based on the username
    @Override
    public List<OrderModel> getOrdersByUsername(String username) {
        List<OrderModel> orders = new ArrayList<>();
        String query = "SELECT * FROM " + TableName + " WHERE username = ? ORDER BY orderDate DESC";

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, username);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                orders.add(new OrderModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("eventId"),
                        rs.getString("eventName"),
                        rs.getInt("quantity"),
                        rs.getDouble("totalPrice"),
                        rs.getString("orderDate")
                ));
            }

        } catch (SQLException e) {
            Notification.showError("Error", "Error retrieving orders");
            System.out.println("Error retrieving orders: " + e.getMessage());
        }

        return orders;
    }

    // retrieve all orders for all the users
    @Override
    public ResultSet getAllOrders() {
        String query = "SELECT * FROM " + TableName + " ORDER BY orderDate DESC";

        try {

            Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
