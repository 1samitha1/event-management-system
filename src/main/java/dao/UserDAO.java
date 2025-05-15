package main.java.dao;

import main.java.model.DatabaseModel;
import main.java.model.UserModel;
import main.java.utils.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements UserDAOInterface {

    // handle user registration
    @Override
    public boolean register(UserModel user) throws SQLException {
        String query = "INSERT INTO users (username, password, preferredName) VALUES (?, ?, ?)";

        try (Connection con = DatabaseModel.getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, user.getUsername());
            st.setString(2, user.getPassword());
            st.setString(3, user.getPreferredName());

            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error during registration: " + e.getMessage());
            return false;
        }
    }

    // handle user login
    @Override
    public UserModel login(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ? ";
        System.out.println(username);
        System.out.println(password);

        try (Connection con = DatabaseModel.getConnection(); PreparedStatement st = con.prepareStatement(query)){
            st.setString(1, username);
            st.setString(2, password);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                // get user details from the query result
                String uname = rs.getString("username");
                String pwd = rs.getString("password");
                String preferred = rs.getString("preferredName");

                return new UserModel(uname, pwd, preferred);
            } else {
                return null;
            }

        } catch (SQLException e){
            Notification.showError("Error", "Database error!");
            return null;
        }
    }
}
