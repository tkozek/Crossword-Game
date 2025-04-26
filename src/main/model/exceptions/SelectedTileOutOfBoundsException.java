package model.exceptions;

public class SelectedTileOutOfBoundsException extends RuntimeException {

    public SelectedTileOutOfBoundsException(String playerName, int index) {
        super("Invalid entry; Index " + index + " is out of bounds for " + playerName + "'s rack");
    }

}
