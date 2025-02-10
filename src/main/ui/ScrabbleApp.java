package ui;

import model.Board;
import model.TileBag;
import model.Player;

// Represents a game of Scrabble

public class ScrabbleApp {
    Player player;

    //EFFECTS: Creates desired number of players and assigns
    //       them each to a common board and tile bag
    public ScrabbleApp(int numPlayers) {
        Board board = new Board();
        TileBag tileBag = new TileBag();
        
        for (int i = 0; i < numPlayers; i++) {
            player = new Player("Player " + Integer.toString(i), board, tileBag);
        }

    }

    // Play the game
    public static void main(String[] args) {
        //!!! 
        //Update later to prompt number of players and use scanner
        new ScrabbleApp(1);
    }
}
