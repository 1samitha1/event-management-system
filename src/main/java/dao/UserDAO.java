package main.java.dao;

import main.java.model.UserModel;
import main.java.utils.Authentication;
import main.java.utils.Notification;

import java.sql.*;

public class UserDAO implements UserDAOInterface {
    private final String TableName = "users";

    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getInstance().getConnection();
             Statement st = connection.createStatement();) {
            String sql = "CREATE TABLE IF NOT EXISTS "  + TableName +  " (username VARCHAR(10) NOT NULL,"
                    + "password VARCHAR(20) NOT NULL, preferredName VARCHAR(10) NOT NULL," + "PRIMARY KEY (username))";
            st.executeUpdate(sql);
        }
    }

    // handle user registration
    @Override
    public boolean register(UserModel user) throws SQLException {
        String query = "INSERT INTO users (username, password, preferredName) VALUES (?, ?, ?)";

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            //hashing password before add into db
            String passwordHash = Authentication.hashPassword(user.getPassword());

            st.setString(1, user.getUsername());
//            st.setString(2, user.getPassword());
            st.setString(2, passwordHash);
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
        String query = "SELECT * FROM " + TableName + " WHERE username = ? AND password = ? ";

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement st = con.prepareStatement(query)){

            // create a matching hash for provided string password
            String hashedPassword = Authentication.hashPassword(password);

            st.setString(1, username);
//            st.setString(2, password);
            //add hashed password to match with db password
            st.setString(2, hashedPassword);

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

    @Override
    public UserModel getUserDetails(String username) {
        String query = "SELECT * FROM " + TableName + " WHERE username = ?";
        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query)) {
            st.setString(1, username);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                // get user details from the query result
                String userName = rs.getString("username");
                String password = rs.getString("password");
                String preferredName = rs.getString("preferredName");

                return new UserModel(userName, password, preferredName);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateUserPassword(String username, String newPassword){
        String query = "UPDATE " + TableName + " SET password = ? WHERE username = ?";

        // creating new hash password
        String newPasswordHash = Authentication.hashPassword(newPassword);

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, newPasswordHash);
            st.setString(2, username);

            // updated new hash password to user
            int rows = st.executeUpdate();

            // return true if record updated
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
