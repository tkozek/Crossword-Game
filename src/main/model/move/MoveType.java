package model.move;

// Represents the type of move performed. A move can either be
// playing a word, swapping between 0 and Player.MAX_NUM_TILES, 
// or subtracting unplayed letters from score at the end of the 
// game
public enum MoveType {
 PLAY_WORD, SWAP_TILES, SKIP//, END_GAME_WINNER, END_GAME_LOSER
}
