package ru.spbau.mit.circuit.model.elements;


public class InvalidCircuitItemException extends RuntimeException {
    public InvalidCircuitItemException() {
    }

    public InvalidCircuitItemException(String message) {
        super(message);
    }
}
