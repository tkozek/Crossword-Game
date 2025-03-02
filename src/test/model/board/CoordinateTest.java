package model.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class CoordinateTest {
    
    private Coordinate c1;
    private Coordinate c2;

    @BeforeEach
    void runBefore() {
        
    }

    @Test
    public void testGetRow() {
        c1 = new Coordinate(0,1);
        assertEquals(c1.getRow(), 0);

        c1 = new Coordinate(10,3);
        assertEquals(c1.getRow(), 10);

        c2 = new Coordinate(4,2);
        assertEquals(4, c2.getRow());

        c2 = new Coordinate(8,5);
        assertEquals(8, c2.getRow());
    }

    @Test
    public void testGetColumn() {
        c1 = new Coordinate(0,1);
        assertEquals(c1.getColumn(), 1);

        c1 = new Coordinate(10,3);
        assertEquals(c1.getColumn(), 3);

        c2 = new Coordinate(4,2);
        assertEquals(2, c2.getColumn());

        c2 = new Coordinate(8,5);
        assertEquals(5, c2.getColumn());
    }

    @Test 
    public void testEqualsEqual() {
        c1 = new Coordinate(0,0);
        c2 = new Coordinate(0,0);
        assertTrue(c1.equals(c2));
        assertTrue(c2.equals(c1));
        assertTrue(c1.equals(c1));

        c1 = new Coordinate(7,7);
        c2 = new Coordinate(7,7);
        assertTrue(c1.equals(c2));
        assertTrue(c2.equals(c1));

        c1 = new Coordinate(3,7);
        c2 = new Coordinate(3,7);
        assertTrue(c1.equals(c2));
        assertTrue(c2.equals(c1));

        c1 = new Coordinate(7,3);
        c2 = new Coordinate(7,3);
        assertTrue(c1.equals(c2));
        assertTrue(c2.equals(c1));
        assertTrue(c1.equals(c1));

        c1 = new Coordinate(1,14);
        c2 = new Coordinate(1,14);
        assertTrue(c1.equals(c2));
        assertTrue(c2.equals(c1));
        assertTrue(c2.equals(c2));
        c1 = new Coordinate(2,0);
        c2 = new Coordinate(2,0);
        assertTrue(c1.equals(c2));
        assertTrue(c2.equals(c1));
    }

    @Test
    public void testEqualsNotSameRow() {
        c1 = new Coordinate(1,0);
        c2 = new Coordinate(0,0);

        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

        c1 = new Coordinate(4,10);
        c2 = new Coordinate(3,10);

        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));


        c1 = new Coordinate(9,3);
        c2 = new Coordinate(10,3);

        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

        c1 = new Coordinate(1,14);
        c2 = new Coordinate(2,14);

        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));
    }

    @Test
    public void testEqualsNotSameCols() {
        c1 = new Coordinate(0,1);
        c2 = new Coordinate(0,0);

        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

        c1 = new Coordinate(3,9);
        c2 = new Coordinate(3,10);

        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));


        c1 = new Coordinate(10,5);
        c2 = new Coordinate(10,3);

        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

        c1 = new Coordinate(2,6);
        c2 = new Coordinate(2,14);

        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));
    }

    @Test
    public void testEqualsRowsAndColsSwapped() {
        c1 = new Coordinate(1,2);
        c2 = new Coordinate(2,1);

        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

        c1 = new Coordinate(3,7);
        c2 = new Coordinate(7,3);

        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

        c1 = new Coordinate(5,8);
        c2 = new Coordinate(8,5);

        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));
        
        c1 = new Coordinate(11,14);
        c2 = new Coordinate(14,11);

        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

        c1 = new Coordinate(3,6);
        c2 = new Coordinate(6,3);

        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

        c1 = new Coordinate(0,14);
        c2 = new Coordinate(14,0);
    }

    @Test
    public void testEqualsDiffRowsDiffColumns() {
        c1 = new Coordinate(3,14);
        c2 = new Coordinate(5,4);
        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

        c1 = new Coordinate(7,2);
        c2 = new Coordinate(8,13);
        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

        c1 = new Coordinate(6,6);
        c2 = new Coordinate(8,8);
        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

        c1 = new Coordinate(2,11);
        c2 = new Coordinate(13,5);
        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

        c1 = new Coordinate(6,9);
        c2 = new Coordinate(12,4);
        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

    }

    @Test 
    public void testHashCodeSameRowSameRow() {
        c1 = new Coordinate(0,0);
        c2 = new Coordinate(0,0);
        assertEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(7,7);
        c2 = new Coordinate(7,7);
        assertEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(3,7);
        c2 = new Coordinate(3,7);
        assertEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(7,3);
        c2 = new Coordinate(7,3);
        assertEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(1,14);
        c2 = new Coordinate(1,14);
        assertEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(2,0);
        c2 = new Coordinate(2,0);
        assertEquals(c1.hashCode(),c2.hashCode());
    }

    @Test
    public void testHashCodeNotSameRow() {
        c1 = new Coordinate(1,0);
        c2 = new Coordinate(0,0);

        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));

        c1 = new Coordinate(4,10);
        c2 = new Coordinate(3,10);
        assertNotEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(9,3);
        c2 = new Coordinate(10,3);

        assertNotEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(1,14);
        c2 = new Coordinate(2,14);

        assertNotEquals(c1.hashCode(),c2.hashCode());
    }

    @Test
    public void testHashCodeNotSameCols() {
        c1 = new Coordinate(0,1);
        c2 = new Coordinate(0,0);

        assertNotEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(3,9);
        c2 = new Coordinate(3,10);

        assertNotEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(10,5);
        c2 = new Coordinate(10,3);

        assertNotEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(2,6);
        c2 = new Coordinate(2,14);

        assertNotEquals(c1.hashCode(),c2.hashCode());
    }

    @Test
    public void testHashCodeRowsAndColsSwapped() {
        c1 = new Coordinate(1,2);
        c2 = new Coordinate(2,1);

        assertNotEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(3,7);
        c2 = new Coordinate(7,3);

        assertNotEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(5,8);
        c2 = new Coordinate(8,5);

        assertNotEquals(c1.hashCode(),c2.hashCode());
        
        c1 = new Coordinate(11,14);
        c2 = new Coordinate(14,11);

        assertNotEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(3,6);
        c2 = new Coordinate(6,3);

        assertNotEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(0,14);
        c2 = new Coordinate(14,0);

        assertNotEquals(c1.hashCode(),c2.hashCode());
    }

    @Test
    public void testHashCodeDiffRowsDiffColumns() {
        c1 = new Coordinate(3,14);
        c2 = new Coordinate(5,4);
        assertNotEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(7,2);
        c2 = new Coordinate(8,13);
        assertNotEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(6,6);
        c2 = new Coordinate(8,8);
        assertNotEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(2,11);
        c2 = new Coordinate(13,5);
        assertNotEquals(c1.hashCode(),c2.hashCode());

        c1 = new Coordinate(6,9);
        c2 = new Coordinate(12,4);
        assertNotEquals(c1.hashCode(),c2.hashCode());
    }
}