package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.java.dao.CartItemDAO;
import main.java.dao.EventDAO;
import main.java.model.CartItemModel;
import main.java.model.EventModel;
import main.java.model.UserModel;
import main.java.utils.Notification;

import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class CartItemController {
    private final Stage stage;
    private UserModel user;
    private String itemName;
    private String itemVenue;
    private EventModel selectedEvent;
    EventDAO eventDAO = new EventDAO();

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
    private TableColumn<CartItemModel, String> cartViewItemName;

    @FXML
    private TableColumn<CartItemModel, Number> cartViewQuantity;

    public CartItemController(Stage stage, UserModel user, EventModel selected) {
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

            // check if the event day is passed with today.
            String dayOfTheEvent = eventDAO.getDayForEvent(this.selectedEvent.getId().get());
            boolean bookableDay = isDayBookable(dayOfTheEvent);

            System.out.println("bookableDay : "+ bookableDay);

            if (dayOfTheEvent == null || !bookableDay) {
                Notification.showError("Day is passed", "You can no longer book this event on (" + dayOfTheEvent + "). Event day is passed!");
                return;
            }

            // check is the entered quantity is valid
            if (quantity <= 0) {
                Notification.showError("Invalid Quantity", "Please enter a valid positive number.");
                return;
            }

            int availableTickets = selectedEvent.getTotalTickets().get() - selectedEvent.getTicketsSold().get();

            // check is there are enough seats for added quantity
            if (quantity > availableTickets) {
                Notification.showError("Not Enough Seats", "Only " + availableTickets + " seats available for the event ");
                return;
            }

            String userName = user.getUsername();
            int eventId = selectedEvent.getId().get();
            String eventName = selectedEvent.getName().get();

            CartItemModel item = new CartItemModel(userName, eventId, quantity, eventName);
            CartItemDAO cartItemDAO = new CartItemDAO();
            boolean result = cartItemDAO.addCartItem(item);

            if(result){
               DashboardController dbc = new DashboardController(this.stage, this.user);
               dbc.displayDashboard(this.user);
               System.out.println("Cart Item added");
            }

        } catch (NumberFormatException e) {
            Notification.showError("Invalid Input", "Please enter a valid quantity.");
        } catch (SQLException | IOException e) {
            Notification.showError("Error", "Application Error");
            throw new RuntimeException(e);
        }
    }

    private boolean isDayBookable(String eventDay3) {
        DayOfWeek eventDay = switch (eventDay3) {
            case "Mon" -> DayOfWeek.MONDAY;
            case "Tue" -> DayOfWeek.TUESDAY;
            case "Wed" -> DayOfWeek.WEDNESDAY;
            case "Thu" -> DayOfWeek.THURSDAY;
            case "Fri" -> DayOfWeek.FRIDAY;
            case "Sat" -> DayOfWeek.SATURDAY;
            case "Sun" -> DayOfWeek.SUNDAY;
            default -> throw new IllegalArgumentException("Invalid day: " + eventDay3);
        };

        DayOfWeek today = LocalDate.now().getDayOfWeek();

        // Check if event day is today or later in the same week
        return eventDay.getValue() >= today.getValue();

    }




//    void displayEventsInCart() throws IOException {
//        // display event selection
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CartView.fxml"));
//        CartItemController cart = new CartItemController(stage, user, this.selectedEvent);
//        loader.setController(cart);
//        Parent root = loader.load();
//        stage.setTitle("Shopping Cart");
//        stage.setScene(new Scene(root));
//    }

}
