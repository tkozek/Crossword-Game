package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.exceptions.BoardSectionUnavailableException;
import model.move.Move;
import model.move.MoveType;
import model.tile.LetterTile;
import model.tile.Tile;

import java.util.List;
import java.util.Map;

public class ScrabbleGameTest {

    private ScrabbleGame game;
    private Player player;
    private Player player2;

    @BeforeEach
    public void setup() {
        game = new ScrabbleGame();
        player = new Player("playerTest");
        player2 = new Player("P");
    }

    @Test
    public void testConstructor() {
        game.addPlayer(player);
        assertEquals(game.getNumPlayers(), 1);
        assertEquals(game.getPlayers().get(0), player);
        assertTrue(game.getMoves().isEmpty());    
        //!!! ToDo Override board and tilebag.equals    
        //assertTrue(game.getBoard().equals(new Board()));
        //assertTrue(game.getTileBag().equals(new TileBag()));
    }

    @Test
    public void testLogSkippedTurnCurrentPlayer() {
        game.addPlayer("Trevor");
        Player trevor = game.getPlayerByIndex(0);
        game.logSkippedTurn();
        assertEquals(1, trevor.getMoves().size());
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
    public void testAddMoves() {
        //tileBag.drawTiles(player);
        List<LetterTile> lettersToPlay = player.getTilesOnRack();

        Move play = new Move(player.getPlayerName(), getStringFromLetters(lettersToPlay), 7, 7, 10, Direction.DOWN);
        game.addMove(play);
        List<Move> moves = game.getMoves();
        assertEquals(moves.size(), 1);
        assertEquals(moves.get(0), play);
        Move skip = new Move(player.getPlayerName());
        game.addMove(skip);
        moves = game.getMoves();
        assertEquals(moves.size(), 2);
        assertEquals(moves.get(1), skip);
        assertEquals(moves.get(0), play);
    }

    @Test
    public void testGetPlayerByName() {
        game.addPlayer(player);
        Player player2 = new Player("otherPlayer");
        game.addPlayer(player2);
        assertEquals(game.getPlayerByName("playerTest"), player);
        assertEquals(game.getPlayerByName("otherPlayer"), player2);
        assertEquals(game.getPlayerByName("NoPlayerwithThisName"), null);
    }

    @Test
    public void testFirstPlayerAndIndexOf() {
        assertEquals(0, game.getCurrentPlayerIndex());
        game.addPlayer(player);
        game.addPlayer(player2);
        game.setCurrentPlayerIndex(1);
        assertEquals(1, game.getCurrentPlayerIndex());
        game.setCurrentPlayer(player);
        assertEquals(0, game.getCurrentPlayerIndex());
        assertEquals(0, game.getPlayerIndex(player));
        assertEquals(1, game.getPlayerIndex(player2));
    }

    @Test 
    public void testGetNumEachCharInBagAndOpponentsNoLettersOnRackOrBoard() {
        //List<LetterTile> letters = new ArrayList<>();
        Map<Character, Integer> counts = game.getNumEachCharInBagAndOpponents(player);
        Map<Character, Integer> drawPileCounts = game.getTileBag().getInitialLetterFrequencies();
        
        // Both should have all keys
        assertEquals(counts.keySet().size(), drawPileCounts.keySet().size());
        
        //.equals for Map should compare that all keys and values are the same
        // in each map by default. which is the expected behaviour for this test
        assertTrue(counts.equals(drawPileCounts));
    }

    @Test
    public void testScorePerpendicularConnectedByBlank() {
        player.addTile(new LetterTile('B', 3));
        player.addTile(new LetterTile('R', 1));
        player.addTile(new LetterTile('I', 1));
        player.addTile(new LetterTile('N', 1));
        player.addTile(new LetterTile('K', 5));
        
        for (int i = 0; i < 5; i++) {
            player.selectTile(i);
        }
        try {
            assertEquals(32, game.playWord(player, 7, 7, Direction.DOWN));
        } catch (BoardSectionUnavailableException e) {
            fail();
        }
        Tile tile = game.getTileAtPositionOnBoard(7, 7);
        assertEquals(3, tile.getPoints());
        assertEquals("B", tile.toDisplay());

        assertEquals(1, game.getMoves().size());
        assertEquals(1, player.getMoves().size());
        assertEquals(player.getNumTilesOnRack(), 7);
        assertEquals(game.getTileBag().numTilesRemaining(), 100 - 7);
        assertEquals(32, player.getPointsThisGame());

        for (int i = 0; i < 7; i++) {
            player.selectTile(i);
        }
        player.removeSelectedTiles();
        assertEquals(player.getNumTilesOnRack(), 0);
        player.addTile(new LetterTile('I', 1));
        player.addTile(new LetterTile('T', 1));
        player.addTile(new LetterTile('E', 1));
        player.addTile(new LetterTile('S', 1));

        assertEquals(player.getNumTilesOnRack(), 4);
        for (int i = 0; i < 4; i++) {
            player.selectTile(i);
        }
        try {
            assertEquals(18, game.playWord(player, 11, 8, Direction.RIGHT));
        } catch (BoardSectionUnavailableException e) {
            fail();
        }
        assertEquals(player.getNumTilesOnRack(), 7);
        for (int i = 0; i < 7; i++) {
            player.selectTile(i);
        }
        player.removeSelectedTiles();
        player.addTile(new LetterTile('-'));
        player.addTile(new LetterTile('I'));
        player.addTile(new LetterTile('T'));
        for (int i = 0; i < 3; i++) {
            player.selectTile(i);
        }
        try {
            assertEquals(8, game.playWord(player, 10, 10, Direction.RIGHT));
        } catch (BoardSectionUnavailableException e) {
            fail();
        }
    }

    @Test
    public void testScorePerpendicularConnectedByBlankOverloadedPlayWord() {
        player.addTile(new LetterTile('B', 3));
        player.addTile(new LetterTile('R', 1));
        player.addTile(new LetterTile('I', 1));
        player.addTile(new LetterTile('N', 1));
        player.addTile(new LetterTile('K', 5));
        
        for (int i = 0; i < 5; i++) {
            player.selectTile(i);
        }
        try {
            game.setStartRow(7);
            game.setStartCol(7);
            game.setDirection(Direction.DOWN);
            assertEquals(32, game.playWord(player));
        } catch (BoardSectionUnavailableException e) {
            fail();
        }
        assertEquals(1, game.getMoves().size());
        assertEquals(1, player.getMoves().size());
        assertEquals(player.getNumTilesOnRack(), 7);
        assertEquals(game.getTileBag().numTilesRemaining(), 100 - 7);
        assertEquals(32, player.getPointsThisGame());

        for (int i = 0; i < 7; i++) {
            player.selectTile(i);
        }
        player.removeSelectedTiles();
        assertEquals(player.getNumTilesOnRack(), 0);
        player.addTile(new LetterTile('I', 1));
        player.addTile(new LetterTile('T', 1));
        player.addTile(new LetterTile('E', 1));
        player.addTile(new LetterTile('S', 1));

        assertEquals(player.getNumTilesOnRack(), 4);
        for (int i = 0; i < 4; i++) {
            player.selectTile(i);
        }
        try {
            game.setStartRow(11);
            game.setStartCol(8);
            game.setDirection(Direction.RIGHT);
            assertEquals(18, game.playWord(player));
        } catch (BoardSectionUnavailableException e) {
            fail();
        }
        assertEquals(player.getNumTilesOnRack(), 7);
        for (int i = 0; i < 7; i++) {
            player.selectTile(i);
        }
        player.removeSelectedTiles();
        player.addTile(new LetterTile('-'));
        player.addTile(new LetterTile('I'));
        player.addTile(new LetterTile('T'));
        for (int i = 0; i < 3; i++) {
            player.selectTile(i);
        }
        try {
            game.setStartRow(10);
            game.setStartCol(10);
            game.setDirection(Direction.RIGHT);
            assertEquals(8, game.playWord(player));
        } catch (BoardSectionUnavailableException e) {
            fail();
        }
    }

    @Test
    public void testEndGameAdjustments() {
        game.addPlayer(player);
        game.addPlayer(player2);
        game.drawTiles(player2);
        assertEquals(player2.getNumTilesOnRack(), 7);
        int scoreToLoseP2 = 0;
        for (LetterTile unplayedLetter : player2.getTilesOnRack()) {
            scoreToLoseP2 += unplayedLetter.getPoints();
        }
        player2.setPoints(scoreToLoseP2);
        //player goes out, gains all those unplayed points
        // player 2 loses them all so p2 has zero points left

        //add 1 point so we are convinced that they didn't just swap entire scores
        player.setPoints(1);
        game.performEndGameAdjustments(player);
        assertEquals(player.getPointsThisGame(), 1 + scoreToLoseP2);
        assertEquals(player2.getPointsThisGame(), 0);
        assertEquals(1, player.getMoves().size());
        assertEquals(1, player2.getMoves().size());
        assertEquals(MoveType.END_GAME_ADJUSTMENT, player2.getMoves().get(0).getMoveType());
        assertEquals(MoveType.END_GAME_ADJUSTMENT, player.getMoves().get(0).getMoveType());

        assertEquals(game.getHighestScoringPlayer(), player);
    }

    @Test
    public void testHighestScoringPlayer() {
        assertEquals(null, game.getHighestScoringPlayer());
        Player p3 = new Player("trevor");
        game.addPlayer(player);
        game.addPlayer(player2);
        game.addPlayer(p3); 
        player.setPoints(2);
        player2.setPoints(3);
        p3.setPoints(1);
        assertEquals(player2, game.getHighestScoringPlayer());
        game.setCurrentPlayerIndex(0);
        assertEquals(0, game.getCurrentPlayerIndex());
        game.nextPlayer();
        assertEquals(1, game.getCurrentPlayerIndex());
        game.nextPlayer();
        assertEquals(2, game.getCurrentPlayerIndex());
        game.nextPlayer();
        assertEquals(0, game.getCurrentPlayerIndex());
    }

    @Test
    public void testGetCensoredLastMoveDescription() {
        game.addPlayer(player);
        game.logSkippedTurn(player);
        assertEquals("playerTest skipped their turn.", game.getCensoredLastMoveDescription());
        game.swapTiles(player);
        assertEquals("playerTest swapped their tiles.", game.getCensoredLastMoveDescription());
    }

    @Test
    public void testGetMoveDescription() {
        game.addPlayer(player);
        game.logSkippedTurn(player);
        assertEquals("playerTest skipped their turn.", game.getCensoredLastMoveDescription());
        game.swapTiles(player);
        assertEquals("playerTest swapped their tiles.", game.getCensoredLastMoveDescription());
    }

    @Test
    public void testGetScoreMap() {
        Player p3 = new Player("trevor");

        game.addPlayer(player);
        game.addPlayer(player2);
        game.addPlayer(p3); 

        player.setPoints(2);
        player2.setPoints(3);
        p3.setPoints(1);
        
        Map<String, Integer> scores = game.getPlayerScoreMap();
        assertEquals(2, scores.get("playerTest"));
        assertEquals(3, scores.get("P"));
        assertEquals(1, scores.get("trevor"));
    }

    @Test
    public void testCoordinateSettersGetters() {
        assertEquals(7, game.getStartRow());
        assertEquals(7, game.getStartCol());
        game.setStartRow(10);
        game.setStartCol(9);
        assertEquals(10, game.getStartRow());
        assertEquals(9, game.getStartCol());
    }

    @Test
    public void testSetAndGetDirection() {
        assertEquals(Direction.DOWN, game.getDirection());
        game.setDirection(Direction.RIGHT);
        assertEquals(Direction.RIGHT, game.getDirection());
        game.setDirection(Direction.DOWN);
        assertEquals(Direction.DOWN, game.getDirection());
    }
}
