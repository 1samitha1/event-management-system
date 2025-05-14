package main.java.model.dao;

import main.java.model.DatabaseModel;
import main.java.model.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean registerNewUser(String username, String password, String preferredName) throws SQLException {
        String query = "INSERT INTO users (username, password, preferredName) VALUES (?, ?, ?)";
//        Connection con = DatabaseModel.getConnection();
//        PreparedStatement st = con.prepareStatement(query);
//        st.setString(1, username);
//        st.setString(2, password);
//        st.setString(3, preferredName);
//
//        st.executeUpdate();
//        return true;

        try (Connection con = DatabaseModel.getConnection(); PreparedStatement st = con.prepareStatement(query)) {
            st.setString(1, username);
            st.setString(2, password);
            st.setString(3, preferredName);

            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public UserModel userLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? password = ? ";

        try (Connection con = DatabaseModel.getConnection(); PreparedStatement st = con.prepareStatement(query)){
            st.setString(1, username);
            st.setString(2, password);

            ResultSet rs = st.executeQuery();


            //if (rs.next()) {
                return new UserModel(rs.getString("username"), rs.getString("password"), rs.getString("preferred_name"));
            //}

        } catch (SQLException e){
            return null;
        }
    }
}
