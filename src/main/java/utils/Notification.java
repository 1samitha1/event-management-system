package main.java.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Notification {
    // Single method for display all error notifications
    public static void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
