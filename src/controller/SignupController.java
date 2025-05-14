package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DAO.UserDAO;

import java.io.IOException;
import java.sql.SQLException;

public class SignupController {
    private Stage stage;

    public SignupController(Stage stage) {
        this.stage = stage;
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

    @FXML
    public void signupHandler(ActionEvent Event) throws SQLException, IOException {
        String usernameField = username.getText();
        String passwordField = password.getText();
        String preferredNameField = preferredName.getText();
        String confirmPasswordField = passwordConf.getText();

        // check confirm for password
        if(!usernameField.isEmpty() && !passwordField.isEmpty() &&
                !confirmPasswordField.isEmpty() && !preferredNameField.isEmpty()){
            if(passwordField.equals(confirmPasswordField)){
                // calling register method
                boolean result = uDAO.registerNewUser(usernameField, passwordField, preferredNameField);
                System.out.println("signup successful!");


            }else {
                System.out.println("Error: Password and Confirm password should be same");
            }

        }else {
            System.out.println("Error: Need to fill all fields");
        }


    }

    @FXML
    public void goToLoginPage(ActionEvent Event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
        LoginController login = new LoginController(stage);
        loader.setController(login);
        Parent root = loader.load();
        stage.setTitle("Login");
        stage.setScene(new Scene(root));


    }
}
