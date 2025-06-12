package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.dao.UserDAO;
import main.java.model.UserModel;
import main.java.utils.Authentication;
import main.java.utils.Notification;

import java.util.Objects;

public class UserController {
    private final Stage stage;
    private final UserDAO userDao;
    private final UserModel user;

    private Scene previousScene;

    @FXML
    private Label preferredName;

    @FXML
    private TextField currentPassword;

    @FXML
    private TextField newPassword;

    @FXML
    private TextField confirmNewPassword;


    public UserController(Stage stage, UserDAO userDao, UserModel user) {
        this.stage = stage;
        this.userDao = userDao;
        this.user = user;
    }

    @FXML
    public void initialize() {
        System.out.println("p name: "+ user.getPreferredName());
        preferredName.setText(user.getPreferredName());
    }

    // setting previous screen to go back
    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    @FXML
    private void backToDashboard() {
        // go back to the previous screen
        if (previousScene != null) {
            stage.setScene(previousScene);
        }
    }

    // update user password
    @FXML
    private void updatePassword(){

        // validate if all password fields are filled
        if(currentPassword.getText().isEmpty() || newPassword.getText().isEmpty()
                || confirmNewPassword.getText().isEmpty()){
            Notification.showError("Require fields are empty", "You need to fill all fields");
            return;
        }

        // validate if new password and confirm password is match
        if(!Objects.equals(newPassword.getText(), confirmNewPassword.getText())){
            Notification.showError("Passwords not matching", "New password and confirm new password should be match");
            return;
        }

        UserModel userDetails = userDao.getUserDetails(user.getUsername());
        String newPasswordText = newPassword.getText();
        String currantPasswordText = currentPassword.getText();
        String currantPasswordHash = Authentication.hashPassword(currantPasswordText);

        // validate if current password is correct
        if(!Objects.equals(currantPasswordHash, userDetails.getPassword())){
            Notification.showError("Passwords is wrong", "Enter correct password!");
            return;
        }

        boolean result = userDao.updateUserPassword(user.getUsername(), newPasswordText);
        if(result){
            backToDashboard();
        }

    }
}
