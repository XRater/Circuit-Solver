package ru.spbau.mit.circuit.model.elements;


public class InvalidElementException extends RuntimeException {
    public InvalidElementException() {
    }

    public InvalidElementException(String message) {
        super(message);
    }
}
