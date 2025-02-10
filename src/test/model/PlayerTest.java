package model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class PlayerTest {
    
    Player testPlayer;

    @BeforeEach
    void runBefore() {
        testPlayer = new Player("Trevor");
    }

    @Test
    void testConstructor() {
        assertEquals(0, testPlayer.getPointsThisGame());
        assertEquals(0, testPlayer.getPlayerMoves().size());
        assertEquals(0, testPlayer.getNumTilesOnRack());

    }

}

