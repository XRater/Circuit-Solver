package ru.spbau.mit.circuit.model.exceptions;


/**
 * If deletion to model was invalid.
 */
public class InvalidCircuitObjectDeletion extends RuntimeException {
    public InvalidCircuitObjectDeletion(String message) {
        super(message);
    }
}
