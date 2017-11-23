package ru.spbau.mit.circuit.model;


public class InvalidCircuitObjectAddition extends RuntimeException {
    public InvalidCircuitObjectAddition() {
        super();
    }

    public InvalidCircuitObjectAddition(String message) {
        super(message);
    }
}
