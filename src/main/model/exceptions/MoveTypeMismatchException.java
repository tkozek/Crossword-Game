package model.exceptions;

import model.move.MoveType;

public class MoveTypeMismatchException extends RuntimeException {

    // public MoveTypeMismatchException(MoveType moveType) {
    //     super("Invalid operation called on move of type: " + moveType);
    // }

    public MoveTypeMismatchException(String methodName, MoveType moveType) {
        super("Method \"" + methodName + "\" called on move of an invalid type: " + moveType);
    }
}
