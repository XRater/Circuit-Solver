package ru.spbau.mit.circuit.model.exceptions;


/**
 * If addition to the model was invalid.
 */
public class InvalidCircuitObjectAddition extends RuntimeException {
    public InvalidCircuitObjectAddition(String message) {
        super(message);
    }
}
