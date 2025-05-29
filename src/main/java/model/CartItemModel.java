package main.java.model;
import javafx.beans.property.*;

public class CartItemModel {
    private final IntegerProperty id          = new SimpleIntegerProperty();
    private final StringProperty  username    = new SimpleStringProperty();
    private final IntegerProperty eventId     = new SimpleIntegerProperty();
    private final IntegerProperty quantity    = new SimpleIntegerProperty();

    // constructor
    public CartItemModel(int id, String user, int eventId, int qty) {
        this.id.set(id);
        this.username.set(user);
        this.eventId.set(eventId);
        this.quantity.set(qty);
    }

    // constructor used before inserting */
    public CartItemModel(String user, int eventId, int qty) {
        this(0, user, eventId, qty);
    }

    // getters
    public int getId()          { return id.get();        }
    public String getUsername() { return username.get();  }
    public int getEventId()     { return eventId.get();   }
    public int getQuantity()    { return quantity.get();  }

    // property getters for cart
    public IntegerProperty quantityProperty() { return quantity; }
   // public IntegerProperty quantityProperty() { return quantity; }

}