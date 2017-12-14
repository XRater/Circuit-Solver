package ru.spbau.mit.circuit.model.exceptions;


public class NodesAreAlreadyConnected extends Exception {
    public NodesAreAlreadyConnected() {
        super();
    }

    public NodesAreAlreadyConnected(String message) {
        super(message);
    }
}
