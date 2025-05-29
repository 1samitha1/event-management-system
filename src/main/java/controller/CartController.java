package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import main.java.dao.CartItemDAO;
import main.java.model.CartItemModel;
import main.java.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;

public class ShoppingCartController {
    private final Stage stage;
    private UserModel user;

    @FXML
    private TableView<CartItemModel> allCartItems;

    @FXML
    private TableColumn<CartItemModel, String> cartViewItemName;

    @FXML
    private TableColumn<CartItemModel, Number> cartViewQuantity;

    public ShoppingCartController(Stage stage, UserModel user) {
        this.stage = stage;
        this.user = user;
    }

    @FXML
    public void initialize() {
        CartItemDAO cartItemDAO = new CartItemDAO();

        cartViewItemName.setCellValueFactory(cell -> cell.getValue().eventNameProperty());
        cartViewQuantity.setCellValueFactory(cell -> cell.getValue().quantityProperty());

        try {
            ObservableList<CartItemModel> observableCart =
                    FXCollections.observableArrayList(cartItemDAO.getItemsForUser(this.user.getUsername()));

            allCartItems.setItems(observableCart);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    void displayEventsInCart() throws IOException {
        // display event selection
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CartView.fxml"));
        ShoppingCartController cart = new ShoppingCartController(this.stage, this.user);
        loader.setController(cart);
        Parent root = loader.load();
        stage.setTitle("Shopping Cart");
        stage.setScene(new Scene(root));
    }
}
