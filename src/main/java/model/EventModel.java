package main.java.model;

import javafx.beans.property.*;

public class EventModel {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty venue;
    private final StringProperty day;
    private final DoubleProperty price;
    private final IntegerProperty ticketsSold;
    private final IntegerProperty totalTickets;
    private final StringProperty status;

    public EventModel(int id, String name, String venue, String day, double price, int ticketsSold, int totalTickets, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.venue = new SimpleStringProperty(venue);
        this.day = new SimpleStringProperty(day);
        this.price = new SimpleDoubleProperty(price);
        this.ticketsSold = new SimpleIntegerProperty(ticketsSold);
        this.totalTickets = new SimpleIntegerProperty(totalTickets);
        this.status = new SimpleStringProperty(status);
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty venueProperty() { return venue; }
    public StringProperty dayProperty() { return day; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty ticketsSoldProperty() { return ticketsSold; }
    public IntegerProperty totalTicketsProperty() { return totalTickets; }
    public IntegerProperty idProperty() { return id; }
    public StringProperty statusProperty() { return status; }

    public IntegerProperty getId() { return id; }
    public StringProperty getName() { return name;}
    public StringProperty getVenue() { return venue;}
    public IntegerProperty getTotalTickets() {return totalTickets;}
    public IntegerProperty getTicketsSold() {return ticketsSold;}
    public StringProperty getStatus() { return status;}
    public StringProperty getDay() { return day;}
    public DoubleProperty getPrice() { return price;}

    // For admin dashboard group events
    private final StringProperty displayVenueDay = new SimpleStringProperty();

    public StringProperty displayVenueDayProperty() {
        return displayVenueDay;
    }

    public void setDisplayVenueDay(String value) {
        displayVenueDay.set(value);
    }

    // update status for view
    public void setStatus(String status) {
        this.status.set(status);
    }
}
