package main.java.model;

import javafx.beans.property.*;

public class OrderModel {
    private int id;
    private String username;
    private String eventName;
    private int eventId;
    private String orderDate;
    private double totalPrice;
    private int quantity;

    // for javafx view
    private final StringProperty usernameProperty;
    private final StringProperty eventNameProperty;
    private final IntegerProperty quantityProperty;
    private final DoubleProperty totalPriceProperty;
    private final StringProperty orderDateProperty;

    public OrderModel(int id, String username, int eventId, String eventName, int quantity, double totalPrice, String orderDate) {
        this.id = id;
        this.username = username;
        this.eventId = eventId;
        this.eventName = eventName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;

        // Initialize JavaFX properties
        this.usernameProperty = new SimpleStringProperty(username);
        this.eventNameProperty = new SimpleStringProperty(eventName);
        this.quantityProperty = new SimpleIntegerProperty(quantity);
        this.totalPriceProperty = new SimpleDoubleProperty(totalPrice);
        this.orderDateProperty = new SimpleStringProperty(orderDate);
    }

    // defaults the id to 0, for adding objects without knowing auto incremented id
    public OrderModel(String username, int eventId, String eventName, int quantity, double totalPrice, String orderDate) {
        this(0, username, eventId, eventName, quantity, totalPrice, orderDate);
    }

    // getter functions
    public int getId() { return id; }
    public String getUsername() { return username; }
    public int getEventId() { return  eventId; }
    public String getEventName() { return eventName; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }
    public String getOrderDate() { return orderDate; }

    //getters for javaFx view
    public StringProperty usernameProperty() { return usernameProperty; }
    public StringProperty eventNameProperty() { return eventNameProperty; }
    public IntegerProperty quantityProperty() { return quantityProperty; }
    public DoubleProperty totalPriceProperty() { return totalPriceProperty; }
    public StringProperty orderDateProperty() { return orderDateProperty; }
}
