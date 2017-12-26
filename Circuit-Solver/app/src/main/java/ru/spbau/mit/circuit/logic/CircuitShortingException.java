package ru.spbau.mit.circuit.logic;


public class CircuitShortingException extends Exception {
    public CircuitShortingException() {
    }

    @SuppressWarnings("unused")
    public CircuitShortingException(String message) {
        super(message);
    }
}
