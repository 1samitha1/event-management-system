package main.java.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.dao.UserDAO;
import main.java.dao.UserDAOInterface;
import main.java.model.UserModel;
import main.java.utils.Notification;

import java.io.IOException;
import java.sql.SQLException;

public class SignupController {
    private final Stage stage;
    private final UserDAOInterface userDAO;

    public SignupController(Stage stage) {
        this.stage = stage;
        this.userDAO = new UserDAO();
    }

    private UserDAO uDAO = new UserDAO();

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField passwordConf;

    @FXML
    private TextField preferredName;

    // execute signup
    @FXML
    public void signupHandler(ActionEvent event) {
        String usernameField = username.getText().trim();
        String passwordField = password.getText();
        String confirmPasswordField = passwordConf.getText();
        String preferredNameField = preferredName.getText().trim();

        // validating required fields
        if (usernameField.isEmpty() || passwordField.isEmpty() ||
                confirmPasswordField.isEmpty() || preferredNameField.isEmpty()) {
            Notification.showError("Error", "All fields are required!");
            return;
        }

        // check for password confirmation
        if (!passwordField.equals(confirmPasswordField)) {
            Notification.showError("Error", "Password and Confirm Password do not match!");
            return;
        }

        // Create UserModel object
        UserModel newUser = new UserModel(usernameField, passwordField, preferredNameField);

        try {
            boolean result = userDAO.register(newUser);
            if (result) {
                System.out.println("Signup successful");
                returnToLogin(event);


            } else {
                Notification.showError("Error", "Registration failed. Try again by adding a different username");

            }
        } catch (SQLException e) {
            Notification.showError("Error", "Database error!");
        }
    }

    @FXML
    private void returnToLogin(ActionEvent event) {
        LoginController lg = new LoginController(stage);
        lg.displayLoginPage(event);
    }

}
