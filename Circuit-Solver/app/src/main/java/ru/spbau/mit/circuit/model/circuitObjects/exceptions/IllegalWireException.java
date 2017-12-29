package ru.spbau.mit.circuit.model.circuitObjects.exceptions;


public class IllegalWireException extends RuntimeException {
    @SuppressWarnings("unused")
    public IllegalWireException() {
        super();
    }

    public IllegalWireException(String message) {
        super(message);
    }
}
