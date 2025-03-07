package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.board.Board;
import model.move.Move;
import model.move.MoveType;
import model.tile.LetterTile;
import model.tile.TileBag;


import java.util.List;
import java.util.ArrayList;

public class HistoryTest {
    
    private Board board;
    private TileBag testBag;
    private Player testPlayer;
    private Player testPlayer2;
    private List<LetterTile> p1Letters;
    private History history;
    private Move moveToAdd;
    private Move otherMoveToAdd;
    private LetterTile B; 
    private LetterTile A;
    private LetterTile Z;
    private ScrabbleGame game;

    @BeforeEach
    void runBefore() {
        testBag = new TileBag();
        board =  new Board();
        game = new ScrabbleGame("alphabet", board, testBag);
        testPlayer = new Player("Trevor", game);
        testPlayer2 = new Player("Rovert", game);
        p1Letters = testPlayer.getTilesOnRack();
        B = new LetterTile('B',3);
        A = new LetterTile('A',1);
        Z = new LetterTile('Z',10);
        history = new History();
        moveToAdd = new Move(testPlayer, getStringFromLetters(p1Letters), 1,8, 10, Direction.DOWN);
        otherMoveToAdd = new Move(testPlayer2);

    }

    // EFFECTS: returns list of letter tiles
    // based on input string
    private String getStringFromLetters(List<LetterTile> letters) {
        String result = "";
        for (LetterTile letter : letters) {
            result += letter.getString();
        }
        return result;
    }  

    @Test
    void testConstructor() {
        assertTrue(history.getMoves().isEmpty());
        assertTrue(history.getListOfWordsPlayedContainingLetter('A').isEmpty());
    }

    @Test
    void testAddMoveOneMove() {
        assertTrue(history.getMoves().isEmpty());
        history.addMove(moveToAdd);
        assertEquals(history.getMoves().size(), 1);
        assertEquals(history.getMoves().get(0), moveToAdd);

    }

    @Test
    void testAddTwoMoves() {
        assertTrue(history.getMoves().isEmpty());
        history.addMove(otherMoveToAdd);
        history.addMove(moveToAdd);
        assertEquals(history.getMoves().size(), 2);
        assertEquals(history.getMoves().get(0), otherMoveToAdd);
        assertEquals(history.getMoves().get(1), moveToAdd);
    }

    @Test
    void testGetListOfMoveContainingLetterOneMove() {
        List<LetterTile> playedLetters = new ArrayList<>();
        playedLetters.add(A);
        playedLetters.add(Z);
        Move doesntHaveB = new Move(testPlayer, getStringFromLetters(playedLetters), 3, 5, 13, Direction.DOWN);
        history.addMove(doesntHaveB);
        assertEquals(history.getMoves().size(), 1);
        assertEquals(0, history.getListOfWordsPlayedContainingLetter('B').size());
        assertEquals(1, history.getListOfWordsPlayedContainingLetter('A').size());
        assertEquals(1, history.getListOfWordsPlayedContainingLetter('Z').size());
    }
    
    @Test
    void testGetListOfMoveContainingLetterTwoMoves() {
        List<LetterTile> playedLetters = new ArrayList<>();
        playedLetters.add(A);
        playedLetters.add(B); // AB
        Move doesntHaveZ = new Move(testPlayer, getStringFromLetters(playedLetters), 7,9, 8,Direction.RIGHT);
        history.addMove(doesntHaveZ);

        List<LetterTile> playedLetters2 = new ArrayList<>();
        playedLetters2.add(A);
        playedLetters2.add(Z); //AZ

        Move doesntHaveB = new Move(testPlayer, getStringFromLetters(playedLetters2), 3, 5, 13, Direction.DOWN);
        history.addMove(doesntHaveB);
        assertEquals(history.getMoves().size(), 2);
        //BOTH HAVE A
        assertEquals(history.getListOfWordsPlayedContainingLetter('A').size(), 2);
        //One has B and One has Z
        assertEquals(history.getListOfWordsPlayedContainingLetter('B').size(),1);
        assertEquals(history.getListOfWordsPlayedContainingLetter('Z').size(),1);
        // Neither have Q
        assertEquals(history.getListOfWordsPlayedContainingLetter('Q').size(),0);

        assertEquals(doesntHaveB, history.getListOfWordsPlayedContainingLetter('A').get(1));
        assertEquals(doesntHaveZ, history.getListOfWordsPlayedContainingLetter('A').get(0));

        assertEquals(history.getListOfWordsPlayedContainingLetter('B').get(0), doesntHaveZ);

    }

    @Test
    void testGetAllWordsPlayed() {
        List<Move> wordsPlayed = testPlayer.getHistory().getMovesWithWordPlayed();
        assertTrue(wordsPlayed.isEmpty());
        // uses selected tiles within player, doesn't need non null tiles selected to make a move
        game.logWord(testPlayer, getStringFromLetters(p1Letters), 7, 7, 10, Direction.RIGHT);
        wordsPlayed = testPlayer.getHistory().getMovesWithWordPlayed();
        assertEquals(wordsPlayed.size(), 1);

        game.logWord(testPlayer, getStringFromLetters(p1Letters), 0, 4, 15, Direction.RIGHT);

        testBag.drawTiles(testPlayer);
        testPlayer.selectTile(1);

        game.logWord(testPlayer, getStringFromLetters(p1Letters), 0, 0, 5, Direction.DOWN);
        wordsPlayed = testPlayer.getHistory().getMovesWithWordPlayed();
        assertEquals(wordsPlayed.size(), 3);
        assertEquals(wordsPlayed.get(0).getMoveType(), MoveType.PLAY_WORD);
        assertEquals(wordsPlayed.get(0).getPointsForMove(), 10);

        assertEquals(wordsPlayed.get(1).getMoveType(), MoveType.PLAY_WORD);
        assertEquals(wordsPlayed.get(1).getPointsForMove(), 15);
        
        assertEquals(wordsPlayed.get(2).getMoveType(), MoveType.PLAY_WORD);
        assertEquals(wordsPlayed.get(2).getPointsForMove(), 5);
    }

    @Test
    void testGetMovesWithWordsPlayedSomeAreSwapsAndSkips() {
        // Adds move that isn't a word played [1]
        game.logSkippedTurn(testPlayer);
        assertTrue(testPlayer.getHistory().getMovesWithWordPlayed().isEmpty());
        testBag.drawTiles(testPlayer);
        //logs word played [1]
        game.logWord(testPlayer, (getStringFromLetters(testPlayer.getTilesOnRack())), 7,7,10,Direction.DOWN);
        assertEquals(testPlayer.getHistory().getMovesWithWordPlayed().size(), 1);
        // Adds move that isn't a word played [2]
        game.logSkippedTurn(testPlayer);
        // Adds move that isn't a word played [3]
        game.logSkippedTurn(testPlayer);
        //logs word played [2]
        game.logWord(testPlayer, (getStringFromLetters(testPlayer.getTilesOnRack())), 9, 9, 14, Direction.RIGHT);
        assertEquals(testPlayer.getHistory().getMovesWithWordPlayed().size(), 2);
    }   
    
}