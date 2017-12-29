package ru.spbau.mit.circuit.model.circuitObjects.exceptions;


/**
 * If element has illegal position.
 */
public class InvalidElementException extends RuntimeException {
    @SuppressWarnings("unused")
    public InvalidElementException() {
    }

    public InvalidElementException(String message) {
        super(message);
    }
}
