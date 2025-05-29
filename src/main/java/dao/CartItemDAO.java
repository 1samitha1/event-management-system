package main.java.dao;

import main.java.model.CartItemModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAO implements CartItemDAOInterface {
    private final String TableName = "cartItems";

    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement st = connection.createStatement();) {
            String sql = "CREATE TABLE IF NOT EXISTS "  + TableName +  " (id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR(15) NOT NULL,\n" +
                    "eventId INTEGER NOT NULL, quantity INTEGER NOT NULL, UNIQUE(username, eventId) FOREIGN KEY(eventId) REFERENCES events(id))";
            st.executeUpdate(sql);
        }
    }

    @Override
    public boolean addCartItem(CartItemModel item) throws SQLException {
        String query = "INSERT INTO " + TableName +  " (username, eventId, quantity) VALUES (?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, item.getUsername());
            st.setInt(2, item.getEventId());
            st.setInt(3, item.getQuantity());

            st.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error while adding to cart: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<CartItemModel> getItemsForUser(String username) throws SQLException {
        List<CartItemModel> items = new ArrayList<>();
        String sql = "SELECT * FROM " + TableName + " WHERE username = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                items.add(new CartItemModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("eventId"),
                        rs.getInt("quantity")
                ));
            }
        }

        return items;
    }

    @Override
    public boolean updateCartItemQuantity(int cartItemId, int newQuantity) throws SQLException {
        String sql = "UPDATE " + TableName + " SET quantity = ? WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, newQuantity);
            st.setInt(2, cartItemId);
            return st.executeUpdate() > 0;
        }
    }

    @Override
    public boolean isQuantityAvailable(int eventId, int requestedQty) throws SQLException {
        int totalReserved = getTotalReservedQuantityForEvent(eventId);

        String sql = "SELECT capacity, tickets_sold FROM events WHERE id = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, eventId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int capacity = rs.getInt("capacity");
                int sold = rs.getInt("tickets_sold");
                int available = capacity - sold - totalReserved;
                return requestedQty <= available;
            } else {
                return false;
            }
        }
    }

    public int getTotalReservedQuantityForEvent(int eventId) throws SQLException {
        String sql = "SELECT SUM(quantity) as total FROM cart_items WHERE eventId = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, eventId);
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getInt("total") : 0;
        }
    }

}
