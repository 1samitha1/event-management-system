package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.dao.CartItemDAO;
import main.java.model.CartItemModel;
import main.java.model.UserModel;
import main.java.utils.Notification;

import java.io.IOException;
import java.sql.SQLException;

public class CartController {
    private final Stage stage;
    private UserModel user;

    @FXML
    private TableView<CartItemModel> cartTable;

    @FXML
    private TableColumn<CartItemModel, String> cartViewItemName;

    @FXML
    private TableColumn<CartItemModel, Number> cartViewQuantity;

    @FXML
    private Button cartCheckout;

    public CartController(Stage stage, UserModel user) {
        this.stage = stage;
        this.user = user;
    }

    @FXML
    public void initialize() {
        CartItemDAO cartItemDAO = new CartItemDAO();

        cartViewItemName.setCellValueFactory(cell -> cell.getValue().eventNameProperty());
        cartViewQuantity.setCellValueFactory(cell -> cell.getValue().quantityProperty());

        try {
            ObservableList<CartItemModel> cartList =
                    FXCollections.observableArrayList(cartItemDAO.getItemsForUser(this.user.getUsername()));

            cartTable.setItems(cartList);

            // if there are no items in car, the checkout button is disabled.
            cartCheckout.setDisable(cartList.isEmpty());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void displayEventsInCart() throws IOException {
        // display event selection
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CartView.fxml"));
        CartController cart = new CartController(this.stage, this.user);
        loader.setController(cart);
        Parent root = loader.load();
        stage.setTitle("Shopping Cart");
        stage.setScene(new Scene(root));
    }


    @FXML
    public void closeCartView() throws IOException {
        DashboardController dbc = new DashboardController(stage, user);
        dbc.displayDashboard(this.user);
    }

    @FXML
    public void displayCheckoutView() throws IOException {
        OrderController oc = new OrderController(user, stage);
        oc.showCheckoutView();
    }

    @FXML
    public void removeCartItem(){
        CartItemModel selectedItem = cartTable.getSelectionModel().getSelectedItem();

        if(selectedItem != null){
            CartItemDAO cartItemDAO = new CartItemDAO();

            // remove from the database
            cartItemDAO.removeCartItem(selectedItem.getId());
            // remove from the view
            cartTable.getItems().remove(selectedItem);
        }else {
            Notification.showWarning("Select item first", "You must select item from the table before remove");
        }

    }

    @FXML
    public void displayCartUpdateView() throws IOException, SQLException {
        CartItemModel selectedItem = cartTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            Notification.showError("Select item", "Choose a cart row first.");
            return;
        }

        FXMLLoader fx = new FXMLLoader(getClass().getResource("/view/UpdateCartItemView.fxml"));
        Parent root = fx.load();

        UpdateCartItemController ctrl = fx.getController();
        ctrl.setCartItem(selectedItem);

        Stage st = new Stage();
        st.setScene(new Scene(root));
        st.setTitle("Update Quantity");
        st.initOwner(cartTable.getScene().getWindow());
        st.initModality(Modality.APPLICATION_MODAL);
        st.showAndWait();

        CartItemDAO ct = new CartItemDAO();

        // refresh table after popup closes
        cartTable.getItems().setAll(
                FXCollections.observableArrayList(ct.getItemsForUser(user.getUsername()))
        );
    }
}
