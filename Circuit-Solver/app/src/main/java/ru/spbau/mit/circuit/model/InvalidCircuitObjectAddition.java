package ru.spbau.mit.circuit.model;


public class InvalidCircuitObjectAddition extends Exception {
    public InvalidCircuitObjectAddition() {
        super();
    }

    public InvalidCircuitObjectAddition(String message) {
        super(message);
    }
}
