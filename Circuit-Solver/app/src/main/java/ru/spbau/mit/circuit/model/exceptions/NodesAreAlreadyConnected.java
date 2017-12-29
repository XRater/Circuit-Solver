package ru.spbau.mit.circuit.model.exceptions;

public class NodesAreAlreadyConnected extends Exception {
    @SuppressWarnings("unused")
    public NodesAreAlreadyConnected() {
        super();
    }

    public NodesAreAlreadyConnected(String message) {
        super(message);
    }
}
