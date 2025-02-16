package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MoveTest {
    
    private Board board;
    private TileBag testBag;
    private Player testPlayer;
    private Player testPlayer2;
    private List<LetterTile> p1Letters;
    private List<LetterTile> p2Letters;
    private Board testBoard;
    private Move playWord;
    private Move swap;
    private Move endGameWinner;
    private Move endGameLoser;
    private String alphabet;
    

    @BeforeEach
    void runBefore() {
        testBag = new TileBag();
        board =  new Board();
        testPlayer = new Player("Trevor", testBoard,testBag);
        testPlayer2 = new Player("Rovert", testBoard,testBag);
        testBag.drawTiles(testPlayer);
        testBag.drawTiles(testPlayer2);
        p1Letters = testPlayer.getTilesOnRack();
        p2Letters = testPlayer2.getTilesOnRack();
        
    }

    @Test
    void testConstructorForPlayedWord() {
        playWord = new Move(testPlayer, board, p1Letters, 7,7, 10, Direction.DOWN);
        assertEquals(playWord.getMoveType(), MoveType.PLAY_WORD);
        assertEquals(playWord.getPointsForMove(), 10);
        assertEquals(playWord.getLettersInvolved(), p1Letters);
        assertEquals(playWord.getDirection(),Direction.DOWN); 

    }
    @Test
    void testMoveContainsCharacter() {
        playWord = new Move(testPlayer, board, p1Letters, 1,8, 10, Direction.DOWN);
        LetterTile letter = p1Letters.get(0);
        char firstChar = letter.getCharacter();
        assertEquals(playWord.getMoveType(), MoveType.PLAY_WORD);
        assertEquals(playWord.getPointsForMove(), 10);
        assertEquals(playWord.getLettersInvolved(), p1Letters);
        assertEquals(playWord.getDirection(),Direction.DOWN);
        assertTrue(playWord.moveContainsLetter(firstChar)); 
        boolean allTrue = true;
        alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ-";
        for (int i = 0; i < 26; i++) {
            if (!playWord.moveContainsLetter(alphabet.charAt(i))) {
                allTrue = false;
                break;
            }
        }
        // Move cannot contain all possible letters since tileRack is less than the size of the alphabet
        assertFalse(allTrue);
    }
    @Test
    void testConstructorForSwappedWord() {
        swap = new Move(testPlayer2, board, p1Letters, p2Letters);
        assertEquals(swap.getMoveType(), MoveType.SWAP_TILES);
        assertEquals(swap.getPointsForMove(), 0);
        p1Letters.addAll(p2Letters);
        assertEquals(swap.getLettersInvolved(),p1Letters);
    }
    @Test
    void testConstructorForEndGameAdjustment() {
        endGameLoser = new Move(testPlayer, board, false, p1Letters, 7);
        assertEquals(endGameLoser.getMoveType(), MoveType.END_GAME_LOSER);

        endGameWinner = new Move(testPlayer2, board, true, p1Letters, 7);
        assertEquals(endGameWinner.getMoveType(),MoveType.END_GAME_WINNER);
    }
    
}