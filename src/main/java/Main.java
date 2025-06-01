package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;

import main.java.controller.LoginController;
import main.java.dao.CartItemDAO;
import main.java.dao.EventDAO;
import main.java.dao.OrderDAO;
import main.java.dao.UserDAO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class Main extends Application {
    public static void main(String [] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        // create user table if not exists
        UserDAO userDAO = new UserDAO();
        userDAO.setup();

        // create events table if not exists and adding events into database
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("events.dat");
        EventDAO eventDao = new EventDAO();
        eventDao.setup();
        eventDao.loadEvents(inputStream);

        // create cartItems table if not exists
        CartItemDAO cartItemDAO = new CartItemDAO();
        cartItemDAO.setup();

        // create orders table if not exists
        OrderDAO orderDAO = new OrderDAO();
        orderDAO.setup();

        // Display Login view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
        LoginController controller = new LoginController(primaryStage);
        loader.setController(controller);
        GridPane root = loader.load();

        Scene scene1 = new Scene(root);
        primaryStage.setScene(scene1);
        primaryStage.setTitle("System Login");
        primaryStage.show();

    }

}