package main.java.model;

public class OrderModel {
    private int id;
    private String username;
    private String eventName;
    private int eventId;
    private String orderDate;
    private double totalPrice;
    private int quantity;

    public OrderModel(int id, String username, int eventId, String eventName, int quantity, double totalPrice, String orderDate) {
        this.id = id;
        this.username = username;
        this.eventId = eventId;
        this.eventName = eventName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
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
}
