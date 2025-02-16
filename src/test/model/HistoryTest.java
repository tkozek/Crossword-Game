package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

public class HistoryTest {
    
    private Board board;
    private TileBag testBag;
    private Set<Character> set;
    private Player testPlayer;
    private Player testPlayer2;
    private Board testBoard;
    private HashMap<Character, Integer> preSwapChars;
    private HashMap<Character, Integer> postSwapChars;
    

    @BeforeEach
    void runBefore() {
        testBag = new TileBag();
        board =  new Board();
        testPlayer = new Player("Trevor", testBoard,testBag);
        testPlayer2 = new Player("Rovert", testBoard,testBag);
        preSwapChars = new HashMap<>();
        postSwapChars = new HashMap<>();
    }

    @Test
    void testConstructor() {

    }
}