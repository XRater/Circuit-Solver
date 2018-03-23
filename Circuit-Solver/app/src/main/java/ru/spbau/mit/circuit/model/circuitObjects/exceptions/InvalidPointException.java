package ru.spbau.mit.circuit.model.circuitObjects.exceptions;


/**
 * Thrown if point was invalid (does not have integer coordinates).
 */
public class InvalidPointException extends RuntimeException {

    @SuppressWarnings("unused")
    public InvalidPointException() {
    }

    public InvalidPointException(String message) {
        super(message);
    }

}
