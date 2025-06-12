package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import main.java.dao.EventDAO;
import main.java.dao.OrderDAO;
import main.java.model.EventModel;
import main.java.model.OrderModel;
import main.java.utils.Notification;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AdminDashboardController {
    private final Stage stage;
    private Scene previousScene;
    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    @FXML
    private Label adminWelcomeMsg;

    // for display all Orders
    @FXML
    private TableView<OrderModel> userOrdersTable;

    @FXML
    private TableColumn<OrderModel, String> userCol;

    @FXML
    private TableColumn<OrderModel, String> eventNameCol;

    @FXML
    private TableColumn<OrderModel, Number> totalPriceCol;

    @FXML
    private TableColumn<OrderModel, Number> quantityCol;

    @FXML
    private TableColumn<OrderModel, String> orderDateCol;

    // for display grouped events
    @FXML
    private TableView<EventModel> eventsTable;

    @FXML
    private TableColumn<EventModel, String> eventCol;

    @FXML
    private TableColumn<EventModel, String> venueDayPriceCol;

    @FXML
    private TableColumn<EventModel, Number> soldTicketsCol;

    @FXML
    private TableColumn<EventModel, Number> totalTicketsCol;

    public AdminDashboardController(Stage stage) {
        this.stage = stage;
    }

    // list for hold all orders
    private final ObservableList<OrderModel> allOrderList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // welcome msg for admin
        if(adminWelcomeMsg != null){
            adminWelcomeMsg.setText("Welcome: Admin");
        }

        // Events for admin events table columns
        if(eventCol != null){
            eventCol.setCellValueFactory(cell -> cell.getValue().nameProperty());
        }

        if(venueDayPriceCol != null){
            venueDayPriceCol.setCellValueFactory(cell -> cell.getValue().venueProperty());
        }

        if(soldTicketsCol != null){
            soldTicketsCol.setCellValueFactory(cell -> cell.getValue().ticketsSoldProperty());
        }

        if(totalTicketsCol != null) {
            totalTicketsCol.setCellValueFactory(cell -> cell.getValue().totalTicketsProperty());
        }

        // loading events for event group table
        loadEventsToTableForAdmin();

        // All orders for all users table columns
        if(userCol != null){
            userCol.setCellValueFactory(cell -> cell.getValue().usernameProperty());
        }

        if(eventNameCol != null){
            eventNameCol.setCellValueFactory(cell -> cell.getValue().eventNameProperty());
        }

        if(totalPriceCol != null){
            totalPriceCol.setCellValueFactory(cell -> cell.getValue().totalPriceProperty());
        }

        if(quantityCol != null){
            quantityCol.setCellValueFactory(cell -> cell.getValue().quantityProperty());
        }

        if(orderDateCol != null){
            orderDateCol.setCellValueFactory(cell -> cell.getValue().orderDateProperty());
        }

        // loading orders for all user orders table
        loadAllOrdersToTable();

    }

    // to view all orders for all the users
    @FXML
    public void viewAllOrders() throws IOException {
        this.previousScene = stage.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OrdersForAllUsersView.fxml"));
        AdminDashboardController adc = new AdminDashboardController(stage);
        adc.setPreviousScene(stage.getScene());
        loader.setController(adc);
        Parent root = loader.load();
        stage.setTitle("All Orders for All Users");
        stage.setScene(new Scene(root));

    }

    // display all order in the table view
    private void loadAllOrdersToTable(){
        OrderDAO orderDAO = new OrderDAO();
        try{
            ResultSet rs = orderDAO.getAllOrders();
            while (rs.next()) {
                OrderModel order = new OrderModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("eventId"),
                        rs.getString("eventName"),
                        rs.getInt("totalPrice"),
                        rs.getInt("quantity"),
                        rs.getString("orderDate")
                );

                allOrderList.add(order);
            }
            // adding data to Table view
            if(userOrdersTable != null){
                userOrdersTable.setItems(allOrderList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // close screen
    @FXML
    public void goBack() {
        if (previousScene != null) {
            stage.setScene(previousScene);
        }
    }

    // load grouped events to event table of the admin dashboard
    private void loadEventsToTableForAdmin() {
        EventDAO eventDao = new EventDAO();
        ObservableList<EventModel> groupedList = FXCollections.observableArrayList();

        try {
            ResultSet rs = eventDao.getAllEvents();

            // Grouping maps by event name
            Map<String, Set<String>> sessionTextMap = new HashMap<>();
            Map<String, Integer> totalTicketsMap = new HashMap<>();
            Map<String, Integer> soldTicketsMap = new HashMap<>();

            while (rs.next()) {
                String name = rs.getString("name");
                String venue = rs.getString("venue");
                String day = rs.getString("day");
                double price = rs.getDouble("price");
                int totalTickets = rs.getInt("total_tickets");
                int soldTickets = rs.getInt("tickets_sold");
                String status = rs.getString("status");

                // Group by just event name
                String eventKey = name;

                // Unique event session string
                String session = venue + " – " + day + " – ($" + price + ")";

                // Adding event session string only once
                sessionTextMap.computeIfAbsent(eventKey, k -> new LinkedHashSet<>()).add(session);
                totalTicketsMap.put(eventKey, totalTicketsMap.getOrDefault(eventKey, 0) + totalTickets);
                soldTicketsMap.put(eventKey, soldTicketsMap.getOrDefault(eventKey, 0) + soldTickets);
            }

            // Create one row per event
            for (String eventName : sessionTextMap.keySet()) {
                String combinedSessions = String.join("\n", sessionTextMap.get(eventName));
                int total = totalTicketsMap.getOrDefault(eventName, 0);
                int sold = soldTicketsMap.getOrDefault(eventName, 0);

                EventModel groupedModel = new EventModel(
                        0,
                        eventName,
                        combinedSessions,
                        "Mon",
                        0.0,
                        sold,
                        total,
                        "active"
                );

                groupedList.add(groupedModel);
            }

            if(eventsTable != null){
                eventsTable.setItems(groupedList);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Handle click events
    @FXML
    public void manageEventsClicked() throws IOException {
        EventModel selectedEvent = eventsTable.getSelectionModel().getSelectedItem();

        // check if an event is selected
        if (selectedEvent == null) {
            Notification.showWarning("You must select an event", "Please select an event group from the table.");
            return;
        }

        String eventName = selectedEvent.getName().get();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SelectedEventGroupView.fxml"));
        EventGroupController egc = new EventGroupController(eventName,stage);
        egc.setPreviousScene(stage.getScene());
        loader.setController(egc);
        Parent root = loader.load();
        stage.setTitle("Manage Event Group");
        stage.setScene(new Scene(root));
    }

    // logout for admin
    @FXML
    public void adminLogout(ActionEvent Event){
        LoginController lc = new LoginController(stage);
        //calling the method to display login view
        lc.displayLoginPage(Event);
    }

    // Add new event popup
    @FXML
    private void displayAddEventPopup() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HandleEventView.fxml"));
        EventController EC = new EventController(stage, null);
        EC.setPreviousScene(stage.getScene());
        EC.setAction("insert");
        loader.setController(EC);
        Parent root = loader.load();
        stage.setTitle("Add New Event");
        stage.setScene(new Scene(root));
    }
}
