package main.java.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderDAO implements OrderDAOInterface {
    private final String TableName = "orders";

    @Override
    public void setup() throws SQLException {

    }
}
