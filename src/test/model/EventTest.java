package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.event.Event;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the Event class
 */
public class EventTest {
    private Event event;
    private Date date;

    // NOTE: these tests might fail if time at which line (2) below is executed
    // is different from time that line (1) is executed. Lines (1) and (2) must
    // run in same millisecond for this test to make sense and pass.

    @BeforeEach
    public void runBefore() {
        event = new Event("Sensor open at door"); // (1)
        date = Calendar.getInstance().getTime(); // (2)
    }

    @Test
    public void testEvent() {
        assertEquals("Sensor open at door", event.getDescription());
        float diff = date.getTime() - event.getDate().getTime();
        System.out.println(diff);
        assertTrue(date.getTime() - event.getDate().getTime() <= 50);
    }

    @Test
    public void testToString() {
        assertEquals(date.toString() + "\n" + "Sensor open at door", event.toString());
    }

    @Test
    public void testEquals() {
        assertFalse(event.equals(null));
        assertFalse(event.equals(1));
    }

    @Test
    public void testHashCode() {
        Event event3 = new Event("Off");
        
        Event event2 = new Event("Sensor open at door");
        assertNotEquals(event2.hashCode(), event3.hashCode());
    }
}
