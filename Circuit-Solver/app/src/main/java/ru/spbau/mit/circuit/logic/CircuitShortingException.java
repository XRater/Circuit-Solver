package ru.spbau.mit.circuit.logic;


public class CircuitShortingException extends Exception {
    public CircuitShortingException() {
    }

    public CircuitShortingException(String message) {
        super(message);
    }
}
