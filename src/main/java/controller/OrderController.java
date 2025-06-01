package main.java.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.dao.CartItemDAO;
import main.java.dao.EventDAO;
import main.java.dao.OrderDAO;
import main.java.model.CartItemModel;
import main.java.model.OrderModel;
import main.java.model.UserModel;
import main.java.utils.Notification;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderController {
    private final UserModel user;
    private final Stage stage;

    CartItemDAO cartDao  = new CartItemDAO();
    EventDAO eventDAO = new EventDAO();
    OrderDAO orderDao = new OrderDAO();

    private double grandTotal;
    private List<CartItemModel> cartLines;

    private Scene previousScene;

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    @FXML
    private Label orderTotal;
    @FXML
    private TextField confirmCode;

    @FXML
    private TableView<OrderModel> allOrdersTable;
    @FXML
    private TableColumn<OrderModel, String> orderNo;
    @FXML
    private TableColumn<OrderModel, String> orderEventAndSeats;
    @FXML
    private TableColumn<OrderModel, String> orderDateTime;
    @FXML
    private TableColumn<OrderModel, Double> orderPrice;

    public OrderController(UserModel user, Stage stage) {
        this.user = user;
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        try {
            cartLines = cartDao.getItemsForUser(user.getUsername());

            // calculating total for added cart items
            grandTotal = cartLines.stream()
                    .mapToDouble(c -> {
                        try {
                            return eventDAO.getPriceForEvent(c.getEventId()) * c.getQuantity();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .sum();

            // display total
            if(orderTotal != null){
                orderTotal.setText(String.format("$ %.2f", grandTotal));
            }


            // load order data into view
            loadOrdersToView(this.user.getUsername());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // function for show checkout screen - can call from other controllers
    public void showCheckoutView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CheckoutView.fxml"));
        OrderController oc = new OrderController(this.user, this.stage);
        loader.setController(oc);
        Parent root = loader.load();
        stage.setTitle("Order Checkout");
        stage.setScene(new Scene(root));
    }

    // close checkout screen and show cart
    @FXML
    public void cancelCheckout() throws IOException {
        CartController cc = new CartController(stage, user);
        cc.displayEventsInCart();
    }

    @FXML
    public void confirmCheckout() {
        try {
            for (CartItemModel c : cartLines) {
                String orderTimeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                OrderModel order = new OrderModel(
                        user.getUsername(),
                        c.getEventId(),
                        c.getEventName(),
                        c.getQuantity(),
                        eventDAO.getPriceForEvent(c.getEventId()) * c.getQuantity(),
                        orderTimeStamp
                );
                boolean result = orderDao.addOrder(order);
                if(result){
                    // if order saved successfully, displaying dashboard again
                    DashboardController dc = new DashboardController(stage, user);
                    dc.displayDashboard(user);
                }
            }
        } catch (SQLException e) {
            Notification.showError("Error", "Error Placing Order.");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void loadOrdersToView(String username){
        //list from ordersDAO
        List<OrderModel> orders = orderDao.getOrdersByUsername(username);

        // List for the view
        ObservableList<OrderModel> data = FXCollections.observableArrayList(orders);

        // bind order number with 4 digits format
        if(orderNo != null) {
            orderNo.setCellValueFactory(cellData -> {
                int id = cellData.getValue().getId();
                return new SimpleStringProperty(String.format("%04d", id));
            });
        }

        // bind order date and time
        if(orderDateTime != null) {
            orderDateTime.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getOrderDate()));
        }

        // bind order event name and number of seats
        if(orderEventAndSeats != null) {
            orderEventAndSeats.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getEventName() + " ( " + cellData.getValue().getQuantity() + " seats )")
            );
        }

        // bind price of the order
        if(orderPrice != null){
            orderPrice.setCellValueFactory(cellData ->
                    new SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject()
            );
        }

        if(allOrdersTable != null){
            allOrdersTable.setItems(data);
        }
    }

    @FXML
    private void goBack() {
        if (previousScene != null) {
            stage.setScene(previousScene);
        }
    }

}
