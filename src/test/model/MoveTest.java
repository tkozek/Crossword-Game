package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.move.*;
import model.tile.LetterTile;


import java.util.List;

public class MoveTest {
    
    private Player testPlayer;
    private Player testPlayer2;
    private List<LetterTile> p1Letters;
    private List<LetterTile> p2Letters;
    private Move playWord;
    private Move swap;
    private String alphabet;
    private ScrabbleGame game;

    @BeforeEach
    void runBefore() {
        game = new ScrabbleGame("alphabet");
        testPlayer = new Player("Trevor");
        testPlayer2 = new Player("Rovert");
        game.drawTiles(testPlayer);
        game.drawTiles(testPlayer2);
        p1Letters = testPlayer.getTilesOnRack();
        p2Letters = testPlayer2.getTilesOnRack();
        
    }

    @Test
    void testConstructorForPlayedWord() {
        playWord = new Move(testPlayer, getStringFromLetters(p1Letters), 7,7, 10, Direction.DOWN);
        assertEquals(playWord.getMoveType(), MoveType.PLAY_WORD);
        assertEquals(playWord.getPlayer(), testPlayer);
        assertEquals(playWord.getPointsForMove(), 10);
        assertEquals(playWord.getLettersInvolved(), getStringFromLetters(p1Letters));
        assertEquals(playWord.getDirection(),Direction.DOWN); 
        assertEquals(playWord.getStartColumn(), 7);
        assertEquals(playWord.getStartRow(), 7);

    }

    @Test
    void testMoveContainsCharacter() {
        playWord = new Move(testPlayer, getStringFromLetters(p1Letters), 1,8, 10, Direction.DOWN);
        LetterTile letter = p1Letters.get(0);
        char firstChar = letter.getCharacter();
        assertEquals(playWord.getMoveType(), MoveType.PLAY_WORD);
        assertEquals(playWord.getPointsForMove(), 10);
        assertEquals(playWord.getLettersInvolved(), getStringFromLetters(p1Letters));
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

    // EFFECTS: returns list of letter tiles
    // based on input string
    private String getStringFromLetters(List<LetterTile> letters) {
        String result = "";
        for (LetterTile letter : letters) {
            result += letter.toDisplay();
        }
        return result;
    }  
    
    @Test
    void testConstructorForSwappedWord() {
        swap = new Move(testPlayer2, getStringFromLetters(p1Letters), getStringFromLetters(p2Letters));
        assertEquals(swap.getPlayer(), testPlayer2);
        assertEquals(swap.getMoveType(), MoveType.SWAP_TILES);
        assertEquals(swap.getPointsForMove(), 0);
        p1Letters.addAll(p2Letters);
        assertEquals(swap.getLettersInvolved(), getStringFromLetters(p1Letters));
    }
    
   /*  @Test
    void testConstructorForEndGameAdjustment() {
        endGameLoser = new Move(testPlayer, board, false, p1Letters, 7);
        assertEquals(endGameLoser.getPlayer(), testPlayer);
        assertEquals(endGameLoser.getMoveType(), MoveType.END_GAME_LOSER);
        assertEquals(endGameLoser.getBoard(),board);
        endGameWinner = new Move(testPlayer2, board, true, p1Letters, 7);
        assertEquals(endGameWinner.getPlayer(), testPlayer2);
        assertEquals(endGameWinner.getMoveType(),MoveType.END_GAME_WINNER);
        assertEquals(endGameWinner.getBoard(),board);
    } */

    @Test
    void testConstructorSkip() {
        Move skip = new Move(testPlayer);
        assertEquals(skip.getPointsForMove(), 0);
        assertEquals(skip.getMoveType(), MoveType.SKIP);
        assertEquals(skip.getPlayer(),testPlayer);
    }

    @Test
    void testGetLastPlayer() {
        Move endGame = new Move(testPlayer, testPlayer2, "ADC", 6);
        assertEquals(testPlayer2, endGame.getLastPlayer());
        assertFalse(testPlayer.equals(endGame.getLastPlayer()));
    }
    
}