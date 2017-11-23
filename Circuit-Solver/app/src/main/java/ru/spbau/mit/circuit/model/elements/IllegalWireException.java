package ru.spbau.mit.circuit.model.elements;


public class IllegalWireException extends RuntimeException {
    public IllegalWireException() {
        super();
    }

    public IllegalWireException(String message) {
        super(message);
    }
}
