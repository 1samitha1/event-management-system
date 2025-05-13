package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupController {
    private Stage stage;

    public SignupController(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void signupHandler(ActionEvent Event){
        System.out.println("signup called");


    }

    @FXML
    public void signupCancelled(ActionEvent Event) throws IOException {
        System.out.println("signup called");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
        DashboardController dashboard = new DashboardController(stage);
        loader.setController(dashboard);
        Parent root = loader.load();
        stage.setScene(new Scene(root));

    }
}
