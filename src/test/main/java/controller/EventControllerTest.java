package main.java.controller;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventControllerTest {
    private EventController controller;

    @BeforeEach
    public void setUp() throws Exception {
        controller = new EventController(null, null);
    }

    // testing with valid day inputs
    @Test
    void testValidDays() {
        assertTrue(controller.validateDays("Mon"));
        assertTrue(controller.validateDays("Tue"));
        assertTrue(controller.validateDays("Wed"));
        assertTrue(controller.validateDays("Thu"));
        assertTrue(controller.validateDays("Fri"));
        assertTrue(controller.validateDays("Sat"));
        assertTrue(controller.validateDays("Sun"));
    }

    // Testing with invalid day inputs
    @Test
    void testInvalidDays() {
        assertFalse(controller.validateDays("Monday"));
        assertFalse(controller.validateDays("monday"));
        assertFalse(controller.validateDays("Tues"));
        assertFalse(controller.validateDays("TU"));
        assertFalse(controller.validateDays("Funday"));
        assertFalse(controller.validateDays(""));
        assertFalse(controller.validateDays("123"));
        assertFalse(controller.validateDays(null));
    }

}