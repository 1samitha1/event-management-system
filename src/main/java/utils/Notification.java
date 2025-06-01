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

    // Single method for display all warning notifications
    public static void showWarning(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
