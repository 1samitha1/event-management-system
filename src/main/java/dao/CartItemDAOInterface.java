package main.java.dao;

import main.java.model.CartItemModel;

import java.sql.SQLException;
import java.util.List;

public interface CartItemDAOInterface {
    void setup() throws SQLException;
    boolean addCartItem(CartItemModel item) throws SQLException;
    List<CartItemModel> getItemsForUser(String username) throws SQLException;
    boolean updateCartItemQuantity(int cartItemId, int newQuantity) throws SQLException;
//    boolean deleteCartItem(int cartItemId) throws SQLException;
    boolean isQuantityAvailable(int eventId, int requestedQty) throws SQLException;
//    int getTotalReservedQuantityForEvent(int eventId) throws SQLException;
}
