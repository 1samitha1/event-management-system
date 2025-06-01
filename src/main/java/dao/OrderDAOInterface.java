package main.java.dao;

import main.java.model.CartItemModel;
import main.java.model.OrderModel;

import java.sql.SQLException;
import java.util.List;

public interface OrderDAOInterface {
    void setup() throws SQLException;
    boolean addOrder(OrderModel order) throws SQLException;
    List<OrderModel> getOrdersByUsername(String username) throws SQLException;
}
