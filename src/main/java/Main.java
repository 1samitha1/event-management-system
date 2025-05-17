package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;

import main.java.controller.LoginController;
import main.java.dao.EventDAO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class Main extends Application {
    public static void main(String [] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        // adding events into database
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("events.dat");
        EventDAO eventDao = new EventDAO();
        eventDao.setup();
        eventDao.loadEvents(inputStream);

        // Display Login view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
        LoginController controller = new LoginController(primaryStage);
        loader.setController(controller);
        GridPane root = loader.load();

        Scene scene1 = new Scene(root);
        primaryStage.setScene(scene1);
        primaryStage.setTitle("Login");
        primaryStage.show();

    }

}