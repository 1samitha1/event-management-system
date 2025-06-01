package main.java.model;
import javafx.beans.property.*;

public class CartItemModel {
    private final IntegerProperty id          = new SimpleIntegerProperty();
    private final StringProperty  username    = new SimpleStringProperty();
    private final IntegerProperty eventId     = new SimpleIntegerProperty();
    private final IntegerProperty quantity    = new SimpleIntegerProperty();
    private final StringProperty  eventName   = new SimpleStringProperty();

    // constructor
    public CartItemModel(int id, String user, int eventId, int qty, String eventName) {
        this.id.set(id);
        this.username.set(user);
        this.eventId.set(eventId);
        this.quantity.set(qty);
        this.eventName.set(eventName);

    }

    // constructor used before inserting */
    public CartItemModel(String user, int eventId, int qty, String eventName) {
        this(0, user, eventId, qty, eventName);
    }

    // getters
    public int getId()          { return id.get();        }
    public String getUsername() { return username.get();  }
    public int getEventId()     { return eventId.get();   }
    public int getQuantity()    { return quantity.get();  }
    public String getEventName()    { return eventName.get();  }

    // property getters for cart
    public IntegerProperty quantityProperty() { return quantity; }
    public StringProperty eventNameProperty()   { return eventName; }

}