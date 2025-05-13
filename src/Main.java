import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;

import controller.LoginController;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {
    public static void main(String [] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {

        //        Connection connection = DriverManager.getConnection("jdbc:sqlite:events.db");


//        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/LoginView.fxml"));
//        GridPane root = loader.load();
//
//        Scene scene1 = new Scene(root);
//        primaryStage.setScene(scene1);
//        primaryStage.setTitle("Login");
//
//        primaryStage.show();


        //in fxml controller: controller.LoginController;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/LoginView.fxml"));
        LoginController controller = new LoginController(primaryStage);
        loader.setController(controller);
        GridPane root = loader.load();

        Scene scene1 = new Scene(root);
        primaryStage.setScene(scene1);
        primaryStage.setTitle("Login");
        primaryStage.show();

    }

}