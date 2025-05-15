package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.java.model.UserModel;



public class DashboardController {
    private final Stage stage;
    private UserModel user;

    public DashboardController(Stage stage, UserModel user) {

        this.stage = stage;
        this.user = user;
    }

    @FXML
    private Label welcomeMsg;

    @FXML
    public void initialize() {
        if(user != null){
            welcomeMsg.setText("Welcome "+ user.getPreferredName());
        }
    }
}
