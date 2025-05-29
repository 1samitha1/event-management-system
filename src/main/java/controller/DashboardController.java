package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.java.dao.EventDAO;
import main.java.model.CartItemModel;
import main.java.model.EventModel;
import main.java.model.UserModel;
import main.java.utils.Notification;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    private final Stage stage;
    private UserModel user;

    public DashboardController(Stage stage, UserModel user) {
        this.stage = stage;
        this.user = user;
    }

    @FXML
    private Label welcomeMsg;

    @FXML
    private TableView<EventModel> eventsTable;
    @FXML
    private TableColumn<EventModel, String> nameColumn;
    @FXML
    private TableColumn<EventModel, String> venueColumn;
    @FXML
    private TableColumn<EventModel, String> dayColumn;
    @FXML
    private TableColumn<EventModel, Number> priceColumn;
    @FXML
    private TableColumn<EventModel, Number> soldTicketsColumn;
    @FXML
    private TableColumn<EventModel, Number> totalTicketsColumn;

    @FXML
    private Button cartButton;

    @FXML
    private Button addToCart;

    // ObservableList for UI updates
    private final ObservableList<EventModel> eventList = FXCollections.observableArrayList();
    private List<CartItemModel> cart = new ArrayList<>();


    @FXML
    public void initialize() {
        if(user != null){
            welcomeMsg.setText("Welcome "+ user.getPreferredName() +".");
        }

        addToCart.disableProperty().bind(
                eventsTable.getSelectionModel().selectedItemProperty().isNull()
        );

        // initializing shopping cart button with an image
        Image cartImage = new Image(getClass().getResourceAsStream("/images/cart.png"));
        ImageView cartImageView = new ImageView(cartImage);
        cartImageView.setFitWidth(17);
        cartImageView.setFitHeight(18);
        cartButton.setGraphic(cartImageView);

        // get data from eventModel
        nameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
        venueColumn.setCellValueFactory(cell -> cell.getValue().venueProperty());
        dayColumn.setCellValueFactory(cell -> cell.getValue().dayProperty());
        priceColumn.setCellValueFactory(cell -> cell.getValue().priceProperty());
        soldTicketsColumn.setCellValueFactory(cell -> cell.getValue().ticketsSoldProperty());
        totalTicketsColumn.setCellValueFactory(cell -> cell.getValue().totalTicketsProperty());

        loadEventsToTable();
    }

    //load events data from database to table view
    private void loadEventsToTable() {
        EventDAO eventDao = new EventDAO();
        try {
            // request events data from database
            ResultSet rs = eventDao.getAllEvents();
            while (rs.next()) {
                EventModel event = new EventModel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("venue"),
                        rs.getString("day"),
                        rs.getDouble("price"),
                        rs.getInt("tickets_sold"),
                        rs.getInt("total_tickets")
                );

                eventList.add(event);
            }
            // adding data to Table view
            eventsTable.setItems(eventList);
        } catch (SQLException e) {
            Notification.showError("Error", "Error while display events table");
        }
    }

    @FXML
    public void logout(ActionEvent Event){
        LoginController lc = new LoginController(stage);
        //calling the method to display login view
        lc.displayLoginPage(Event);
    }

    // adding selected event to the shopping cart
    @FXML
    public void displaySelection() throws IOException {
        EventModel selected = eventsTable.getSelectionModel().getSelectedItem();


        // display an error msg if there are no selected event.
        if (selected == null) {
            Notification.showError("Error", "Please select an event first.");
            return;
        }

        System.out.println("Selected Event: " + selected.getName());
        String itemName = selected.getName().get();
        String itemVenue = selected.getVenue().get();

        // display event selection
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddCartItemView.fxml"));
        CartController cart = new CartController(stage, user, selected);
        loader.setController(cart);
        Parent root = loader.load();
        stage.setTitle("Add to cart");
        stage.setScene(new Scene(root));
    }

    public void openShoppingCart() {

    }

    public void displayDashboard(UserModel useData) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardView.fxml"));
        DashboardController dashboard = new DashboardController(stage, useData);
        loader.setController(dashboard);
        Parent root = loader.load();
        stage.setTitle("Dashboard");
        stage.setScene(new Scene(root));
    }


}
