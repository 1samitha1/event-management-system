package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.dao.EventDAO;
import main.java.model.EventModel;
import main.java.utils.Notification;

import java.io.IOException;
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

    @FXML
    private Button enableEvent;

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

    // load events to table
    public void loadEvents(){
        List<EventModel> eventListForName = eventDao.getEventsByName(eventName);
        ObservableList<EventModel> observableList = FXCollections.observableArrayList(eventListForName);
        eventGroupTable.setItems(observableList);
    }

    // disable an event
    @FXML
    private void disableEvent(){
        EventModel selectedEvent = eventGroupTable.getSelectionModel().getSelectedItem();
        if(selectedEvent == null){
            Notification.showWarning("No event selected", "Select an active event from the list");
            return;
        }

        if(selectedEvent.statusProperty().get().equals("active")){
            updateEventStatus("inactive", selectedEvent.getId().get());
        }else {
            Notification.showWarning("Event is inactive", "This event is already inactive");
        }
    }

    // enable a disabled event
    @FXML
    private void enableEvent(){
        EventModel selectedEvent = eventGroupTable.getSelectionModel().getSelectedItem();
        if(selectedEvent == null){
            Notification.showWarning("No event selected", "Select an inactive event from the list");
            return;
        }

        if(selectedEvent.statusProperty().get().equals("inactive")){
            updateEventStatus("active", selectedEvent.getId().get());
        }else {
            Notification.showWarning("Event is active", "This event is already active");
        }

    }

    // change event status
    public void updateEventStatus(String status, int eventId){
        EventModel selectedEvent = eventGroupTable.getSelectionModel().getSelectedItem();

        // call function to update status in database
        boolean result = eventDao.changeEventStatus(selectedEvent.getId().get(), status);

        if(result){
            // updating updated status in table
            selectedEvent.setStatus(status);
            eventGroupTable.refresh();
        }
    }

    // update an event
    @FXML
    private void displayUpdateEventPopup() throws IOException {
        EventModel selectedEvent = eventGroupTable.getSelectionModel().getSelectedItem();

        // validate if admin selected a group from the list
        if(selectedEvent != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HandleEventView.fxml"));
            EventController EC = new EventController(stage, selectedEvent);
            EC.setPreviousScene(stage.getScene());
            // setting the action type
            EC.setAction("update");
            loader.setController(EC);
            Parent root = loader.load();
            stage.setTitle("Update Event");
            stage.setScene(new Scene(root));
        }else {
            Notification.showWarning("No event selected", "You must select an event first");
        }

    }

    // deleting an event
    @FXML
    private void deleteSelectedEvent() throws IOException {
        EventModel selectedEvent = eventGroupTable.getSelectionModel().getSelectedItem();

        if(selectedEvent == null){
            Notification.showWarning("No event Selected", "You must select an event first");
            return;
        }

        EventController EC = new EventController(stage, selectedEvent);
        EC.deleteEvent();
    }
}
