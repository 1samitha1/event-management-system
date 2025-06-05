package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import main.java.dao.EventDAO;
import main.java.model.EventModel;
import main.java.utils.Notification;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EventGroupController {
    private String eventName;
    private final Stage stage;
    private Scene previousScene;

    EventDAO eventDao = new EventDAO();

    @FXML
    private TableView<EventModel> eventGroupTable;

    @FXML
    private TableColumn<EventModel, Number> EGid;

    @FXML
    private TableColumn<EventModel, String> EGName;

    @FXML
    private TableColumn<EventModel, String> EGVenue;

    @FXML
    private TableColumn<EventModel, String> EGDay;

    @FXML
    private TableColumn<EventModel, Number> EGPrice;

    @FXML
    private TableColumn<EventModel, Number> EGSolidTickets;

    @FXML
    private TableColumn<EventModel, Number> EGTotalTickets;

    @FXML
    private TableColumn<EventModel, String> EGStatus;


    public EventGroupController(String eventName, Stage stage) {
        this.eventName = eventName;
        this.stage = stage;
    }

    @FXML
    public void initialize() {

        if(EGName != null){
            EGid.setCellValueFactory(cell -> cell.getValue().idProperty());
        }

        if(EGName != null){
            EGName.setCellValueFactory(cell -> cell.getValue().nameProperty());
        }

        if(EGVenue != null){
            EGVenue.setCellValueFactory(cell -> cell.getValue().venueProperty());
        }

        if(EGDay != null){
            EGDay.setCellValueFactory(cell -> cell.getValue().dayProperty());
        }

        if(EGPrice != null){
            EGPrice.setCellValueFactory(cell -> cell.getValue().priceProperty());
        }

        if(EGSolidTickets != null){
            EGSolidTickets.setCellValueFactory(cell -> cell.getValue().ticketsSoldProperty());
        }

        if(EGTotalTickets != null){
            EGTotalTickets.setCellValueFactory(cell -> cell.getValue().totalTicketsProperty());
        }

        if(EGStatus != null){
            EGStatus.setCellValueFactory(cell -> cell.getValue().statusProperty());
        }

        loadEvents();

    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    @FXML
    private void goBack() {
        // go back to the previous screen
        if (previousScene != null) {
            stage.setScene(previousScene);
        }
    }

    private void loadEvents(){
        List<EventModel> eventListForName = eventDao.getEventsByName(eventName);
        ObservableList<EventModel> observableList = FXCollections.observableArrayList(eventListForName);
        eventGroupTable.setItems(observableList);
    }

    @FXML
    private void disableEvent(){
        updateEventStatus("inactive");
    }

    @FXML
    private void enableEvent(){
        updateEventStatus("active");
    }

    private void updateEventStatus(String status){
        EventModel selectedEvent = eventGroupTable.getSelectionModel().getSelectedItem();

        // call function to update status in database
        boolean result = eventDao.changeEventStatus(selectedEvent.getId().get(), status);

        if(result){
            // updating updated status in table
            selectedEvent.setStatus(status);
            eventGroupTable.refresh();
        }

    }
}
