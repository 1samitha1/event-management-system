package main.java.dao;

import main.java.model.UserModel;
import java.sql.SQLException;

// Interface for UserDao
public interface UserDAOInterface {
    void setup() throws SQLException;
    boolean register(UserModel user) throws SQLException;
    UserModel login (String username, String password) throws SQLException;
    UserModel getUserDetails (String username) throws SQLException;
    boolean updateUserPassword(String username, String newPassword) throws SQLException;
}
