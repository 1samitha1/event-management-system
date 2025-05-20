package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.dao.EventDAO;
import main.java.model.EventModel;
import main.java.model.UserModel;
import main.java.utils.Notification;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    // ObservableList for UI updates
    private final ObservableList<EventModel> eventList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        if(user != null){
            welcomeMsg.setText("Welcome "+ user.getPreferredName() +".");
        }

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
}
