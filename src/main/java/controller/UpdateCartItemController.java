package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.dao.CartItemDAO;
import main.java.model.CartItemModel;
import main.java.utils.Notification;

import java.sql.SQLException;

public class UpdateCartItemController {
    //private final Stage stage;
    private CartItemModel item;

    @FXML
    private Label eventNameUpdate;

    @FXML
    private TextField quantityUpdate;

    private final CartItemDAO cartItemDao = new CartItemDAO();
    private CartItemModel cartItemModel;

    // Constructor for FXMLLoader
    public UpdateCartItemController() {

    }

    public void setCartItem(CartItemModel item) {
        this.item = item;
        eventNameUpdate.setText(item.getEventName());
        quantityUpdate.setText(String.valueOf(item.getQuantity()));
    }


    @FXML
    public void updateQuantity(){
        int quantityNum = Integer.parseInt(quantityUpdate.getText());

        try {
            if(quantityNum == 0 || quantityNum < 0){
                Notification.showError("Quantity invalid", "Quantity must be a positive number and more than 0");

            }else {
                boolean result = cartItemDao.updateCartItemQuantity(this.item.getId(), quantityNum);
                if(result){
                    close();
                }
            }
        } catch (SQLException e) {
            Notification.showError("Error", "Error updating the quantity for selected item");
            throw new RuntimeException(e);
        }


    }

    @FXML
    private void close() {
        Stage st = (Stage) quantityUpdate.getScene().getWindow();
        st.close();
    }
}
