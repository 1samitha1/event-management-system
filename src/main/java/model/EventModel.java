package main.java.model;

import javafx.beans.property.*;

public class EventModel {
    private final StringProperty name;
    private final StringProperty venue;
    private final StringProperty day;
    private final DoubleProperty price;
    private final IntegerProperty ticketsSold;
    private final IntegerProperty totalTickets;

    public EventModel(String name, String venue, String day, double price, int ticketsSold, int totalTickets) {
        this.name = new SimpleStringProperty(name);
        this.venue = new SimpleStringProperty(venue);
        this.day = new SimpleStringProperty(day);
        this.price = new SimpleDoubleProperty(price);
        this.ticketsSold = new SimpleIntegerProperty(ticketsSold);
        this.totalTickets = new SimpleIntegerProperty(totalTickets);
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty venueProperty() { return venue; }
    public StringProperty dayProperty() { return day; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty ticketsSoldProperty() { return ticketsSold; }
    public IntegerProperty totalTicketsProperty() { return totalTickets; }

}
