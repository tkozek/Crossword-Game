package model.tile;

// Categorizes board tiles' ability to multiply letter or word scores
// NORMAL doesn't change anything
// DOUBLE_LETTER doubles the points scored for a letter placed on it
// TRIPLE_LETTER does the same but it triples the points
// DOUBLE_WORD doubles the points scored for the first move that intersects it.
// TRIPLE_WORD does the same but triples the points
public enum TileType {

    NORMAL, DOUBLE_LETTER, DOUBLE_WORD, TRIPLE_LETTER, TRIPLE_WORD
}
