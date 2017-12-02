package ru.spbau.mit.circuit.model.elements;


public class IllegalWireException extends Exception {
    public IllegalWireException() {
        super();
    }

    public IllegalWireException(String message) {
        super(message);
    }
}
