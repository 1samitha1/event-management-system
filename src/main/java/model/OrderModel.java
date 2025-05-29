package main.java.model;

public class OrderModel {
    private int id;
    private String username;
    private String status;

    public OrderModel(int id, String username, String status) {
        this.id = id;
        this.username = username;
        this.status = status;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getStatus() { return status; }
}
