package main.java.controller;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.java.dao.CartItemDAO;
import main.java.model.CartItemModel;
import main.java.model.EventModel;
import main.java.model.UserModel;
import main.java.utils.Notification;

import java.io.IOException;
import java.sql.SQLException;

public class CartController {
    private final Stage stage;
    private UserModel user;
    private String itemName;
    private String itemVenue;
    private EventModel selectedEvent;

    @FXML
    private ImageView shoppingCart;

    @FXML
    private Label itemNameLabel;

    @FXML
    private Label itemVenueLabel;

    @FXML
    private TextField addedQuantity;

    @FXML
    private TableView<CartItemModel> allCartItems;
    @FXML
    private TableColumn<CartItemModel, Number> quantityColumn;

    public CartController(Stage stage, UserModel user, EventModel selected) {
        this.stage = stage;
        this.user = user;
        this.itemName = selected.getName().get();
        this.itemVenue = selected.getVenue().get();
        this.selectedEvent = selected;
    }


    @FXML
    public void initialize() {
        itemNameLabel.setText(this.itemName);
        itemVenueLabel.setText(this.itemVenue);
    }

    // close add to cart popup
    @FXML
    public void closeAddToCartPopUp(ActionEvent Event) throws IOException {
        DashboardController dbc = new DashboardController(this.stage, this.user);
        dbc.displayDashboard(this.user);
    }

    // adding selected event to the cart
    public void addEventToCart() {

        try {
            int quantity = Integer.parseInt(addedQuantity.getText());

            if (quantity <= 0) {
                Notification.showError("Invalid Quantity", "Please enter a valid positive number.");
                return;
            }

            int availableTickets = selectedEvent.getTotalTickets().get() - selectedEvent.getTicketsSold().get();

            if (quantity > availableTickets) {
                Notification.showError("Not Enough Seats", "Only " + availableTickets + " seats available for " + selectedEvent.getName());
                return;
            }

            String userName = user.getUsername();
            int eventId = selectedEvent.getId().get();

            CartItemModel item = new CartItemModel(userName, eventId, quantity);
            CartItemDAO cartItemDAO = new CartItemDAO();
            boolean result = cartItemDAO.addCartItem(item);

            if(result){
               DashboardController dbc = new DashboardController(this.stage, this.user);
               dbc.displayDashboard(this.user);
            }
            System.out.println("Cart Item added");


        } catch (NumberFormatException e) {
            Notification.showError("Invalid Input", "Please enter a valid quantity.");
        } catch (SQLException e) {
            Notification.showError("Error", "Application Error");
            throw new RuntimeException(e);
        } catch (IOException e) {
            Notification.showError("Error", "Application Error");
            throw new RuntimeException(e);
        }
    }

    void displayEventsInCart(){

    }

}
