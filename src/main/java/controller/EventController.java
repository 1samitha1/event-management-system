package main.java.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.dao.EventDAO;
import main.java.dao.OrderDAO;
import main.java.model.EventModel;
import main.java.utils.Notification;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EventController {
    private final Stage stage;
    private EventModel selectedEvent;
    private String action;
    public EventDAO eventDAO = new EventDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private Scene previousScene;

    @FXML
    private TextField eventname;

    @FXML
    private TextField eventVenue;

    @FXML
    private TextField eventDay;

    @FXML
    private TextField eventPrice;

    @FXML
    private TextField eventCapacity;

    public EventController(Stage stage, EventModel selectedEvent) {
        this.stage = stage;
        this.selectedEvent = selectedEvent;
    }

    @FXML
    public void initialize() {

        if(this.selectedEvent != null){
            if(eventname != null){
                eventname.setText(this.selectedEvent.getName().get());
            }

            if(eventVenue != null){
                eventVenue.setText(this.selectedEvent.getVenue().get());
            }

            if(eventDay != null){
                eventDay.setText(this.selectedEvent.getDay().get());
            }

            if(eventPrice != null){
                eventPrice.setText(String.valueOf(this.selectedEvent.getPrice().get()));
            }

            if(eventCapacity != null){
                eventCapacity.setText(String.valueOf(this.selectedEvent.getTotalTickets().get()));
            }
        }

    }

    public void setAction(String action){
        this.action = action;
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    @FXML
    public void proceedAction() throws IOException {

        // get Text values filled
        String eventName = eventname.getText().trim();
        String venue = eventVenue.getText().trim();
        String day = eventDay.getText().trim();
        String priceTxt = eventPrice.getText().trim();
        String capacityText = eventCapacity.getText().trim();
        double price;
        int quantity;

        // check if all inputs filed
        if(eventName.isEmpty() || venue.isEmpty() || day.isEmpty() || priceTxt.isEmpty() || capacityText.isEmpty()){
            Notification.showWarning("Invalid inputs", "All fields must filed");
            return;
        }

        // check if the day is a valid day
        boolean dayValidation = validateDays(day);
        if(!dayValidation){
            Notification.showWarning("Invalid Day", "Please enter a valid day with 3 letter format (e.g., Mon, Tue, Wed).");
            return;
        }

        // check if price and quantity are valid number inputs
        try {
            price = Double.parseDouble(priceTxt);
            quantity = Integer.parseInt(capacityText);
        } catch (NumberFormatException e) {
            Notification.showWarning("Invalid Number", "Price and Capacity must be valid numbers.");
            return;
        }

        // check if price and quantity are valid
        if(price <= 0 || quantity <= 0){
            Notification.showWarning("Invalid inputs", "Price and Total tickets must be positive numbers");
            return;
        }

        // for event updates
        if("update".equals(this.action)){

            int eventId = selectedEvent.getId().get();
            int ticketsSold = selectedEvent.getTicketsSold().get();
            String status = selectedEvent.getStatus().get();

            // create object
            EventModel updatedEvent = new EventModel(eventId, eventName, venue, day, price, ticketsSold, quantity, status);
            // call dao function for update
            boolean updateRes =  eventDAO.updateEvent(updatedEvent);
            if(updateRes){
                close();
            }else {
                Notification.showError("Cant update Event", "Event update Failed");
            }

        }else {
            // for create new event

            int ticketsSold = 0;
            String status = "active";

            // create object
            EventModel newEvent = new EventModel(0,eventName, venue, day, price, ticketsSold, quantity, status);
            boolean insertRes = eventDAO.createNewEvent(newEvent);
            if(insertRes){
                close();
            }else {
                Notification.showError("Cant create Event", "Event is already exists");
            }
        }
    }

    // helper method for validate days
    public boolean validateDays(String dayInput){
        String[] validDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        Set<String> validDaySet = new HashSet<>(Arrays.asList(validDays));

        if (!validDaySet.contains(dayInput)) {
           return false;
        }else {
            return true;
        }
    }

    @FXML
    private void close() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminDashboardView.fxml"));
        AdminDashboardController adminController = new AdminDashboardController(stage);
        loader.setController(adminController);
        Parent root = loader.load();
        stage.setTitle("Admin Dashboard");
        stage.setScene(new Scene(root));
    }

    // delete selected event
    public void deleteEvent() throws IOException {
        int eventId = selectedEvent.getId().get();
        boolean hasOrders = orderDAO.isOrderAvailableForEvent(eventId);

        /* since I have follow relational db architecture, here the orders table has a reference with id of eventsInfo table.
           because every orders related to an event. Hence, when an event deletes, the orders related to that event also have to delete.
           But before delete I will take a confirmation from Admin if there are related orders for the event.
        */

        if (hasOrders) {
            boolean userConformation = Notification.showConfirmation(
                    "Orders Found",
                    "This event has existing orders.",
                    "Deleting this event will also remove related orders. Are you sure you want to delete the event?"
            );

            if(userConformation){
                // confirmation received to delete
                // first, delete the orders

                orderDAO.deleteOrderByEvent(eventId);
                // Then delete the event
                eventDAO.deleteEvent(eventId);
                close();
            }

        }else {
            // no related orderes, directly can delete the event
            boolean userConformation = Notification.showConfirmation(
                    "Deleting Event",
                    "This event is about to permanently delete.",
                    "Are you sure you want to continue?"
            );

            if(userConformation){
                // delete event
                eventDAO.deleteEvent(eventId);
                close();
            }
        }
    }
}
