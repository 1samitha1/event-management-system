package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.dao.UserDAO;
import main.java.dao.UserDAOInterface;
import main.java.model.UserModel;
import main.java.utils.Notification;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    private final Stage stage;
    private final UserDAOInterface UserDAO;
    private String adminUsername = "admin";
    private String adminPass = "Admin321";

    //private UserDAO uDAO = new UserDAO();

    public LoginController(Stage stage) {

        this.stage = stage;
        this.UserDAO = new UserDAO();
    }

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    public void loginHandler(ActionEvent Event) throws IOException {
        String userNameVal = username.getText();
        String passwordVal = password.getText();

        // check is both username and password fields are filled
        if(userNameVal.isEmpty() || passwordVal.isEmpty()){
            Notification.showError("Error", "Username or Password is empty!");
            return;
        }

        // if its Admin, check pre defined credentials
        if (userNameVal.equalsIgnoreCase(adminUsername) && passwordVal.equals(adminPass)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminDashboardView.fxml"));
            AdminDashboardController adminController = new AdminDashboardController(stage);
            loader.setController(adminController);
            Parent root = loader.load();
            stage.setTitle("Admin Dashboard");
            stage.setScene(new Scene(root));
            return;
        }

        try {
            UserModel result = UserDAO.login(userNameVal, passwordVal);

            // User found
            if(result != null) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardView.fxml"));
                DashboardController dashboard = new DashboardController(stage, result);
                loader.setController(dashboard);
                Parent root = loader.load();
                stage.setTitle("Dashboard");
                stage.setScene(new Scene(root));
            }else {
                Notification.showError("Error", "Login Error!");
            }

        }catch (SQLException e){
            Notification.showError("Error", "Database error!");
        }
    };

    @FXML
    public void displaySignupView(ActionEvent Event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignupView.fxml"));
        SignupController signup = new SignupController(stage);
        loader.setController(signup);
        Parent root = loader.load();
        stage.setTitle("Signup");
        stage.setScene(new Scene(root));

    };

    // This method display the login view when its called
    public void displayLoginPage(ActionEvent Event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            LoginController login = new LoginController(stage);
            loader.setController(login);
            Parent root = loader.load();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            Notification.showError("Error", "System error!");
        }
    }
}
