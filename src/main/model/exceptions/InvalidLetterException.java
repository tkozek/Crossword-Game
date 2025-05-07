package model.exceptions;

public class InvalidLetterException extends RuntimeException {

    // public InvalidLetterException() {
    //     super();
    // }

    public InvalidLetterException(char letter) {
        super("Invalid letter: \"" + String.valueOf(letter) + "\"");
    }

    // public InvalidLetterException(String letter) {
    //     super("Invalid letter: " + letter);
    // }
}
