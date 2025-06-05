package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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

    private final ObservableList<OrderModel> allOrderList = FXCollections.observableArrayList();

    private final ObservableList<EventModel> groupedEvents = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if(adminWelcomeMsg != null){
            adminWelcomeMsg.setText("Welcome: Admin");
        }

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

        // for all user orders
        loadAllOrdersToTable();

        eventCol.setCellValueFactory(cell -> cell.getValue().nameProperty());
        venueDayPriceCol.setCellValueFactory(cell -> cell.getValue().venueProperty()); // sessions
        soldTicketsCol.setCellValueFactory(cell -> cell.getValue().ticketsSoldProperty());
        totalTicketsCol.setCellValueFactory(cell -> cell.getValue().totalTicketsProperty());

        // for event group table
        loadEventsToTableForAdmin();

    }

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
                OrderModel event = new OrderModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("eventId"),
                        rs.getString("eventName"),
                        rs.getInt("totalPrice"),
                        rs.getInt("quantity"),
                        rs.getString("orderDate")
                );

                allOrderList.add(event);
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
        System.out.println("1");
        if (previousScene != null) {
            System.out.println("2");
            stage.setScene(previousScene);
        }
    }

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

                // Unique session string
                String session = venue + " – " + day + " – ($" + price + ")";

                // Add session string only once
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

            eventsTable.setItems(groupedList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void manageEventsClicked() throws IOException {
        EventModel selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        String eventName = selectedEvent.getName().get();

        if (selectedEvent == null) {
            Notification.showWarning("You must select an event", "Please select an event group from the table.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SelectedEventGroupView.fxml"));
        EventGroupController egc = new EventGroupController(eventName,stage);
        egc.setPreviousScene(stage.getScene());
        loader.setController(egc);
        Parent root = loader.load();
        stage.setTitle("Manage event group");
        stage.setScene(new Scene(root));


    }


}
