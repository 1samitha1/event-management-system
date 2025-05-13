package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private Stage stage;

    public LoginController(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    public void loginHandler(ActionEvent Event) throws IOException {

        System.out.println("username : "+ username.getText());
        System.out.println("password : "+ password.getText());

        if(username.getText().equals("samitha") && password.getText().equals("123")){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardView2.fxml"));
            DashboardController dashboard = new DashboardController(stage);
            loader.setController(dashboard);
            Parent root = loader.load();
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(root));
        }else {
            System.out.println("Values empty ");
        }


    };

    @FXML
    public void displaySignupView(ActionEvent Event) throws IOException {
        System.out.println("signup called");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignupView.fxml"));
        DashboardController dashboard = new DashboardController(stage);
        loader.setController(dashboard);
        Parent root = loader.load();
        stage.setTitle("Signup");
        stage.setScene(new Scene(root));

    };
}
