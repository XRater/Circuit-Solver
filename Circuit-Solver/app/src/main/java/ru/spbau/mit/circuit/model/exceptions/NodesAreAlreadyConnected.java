package ru.spbau.mit.circuit.model.exceptions;

/**
 * Exceptions happens if you are trying to connect 2 nodes that are already connected.
 */
public class NodesAreAlreadyConnected extends Exception {
    @SuppressWarnings("unused")
    public NodesAreAlreadyConnected() {
        super();
    }

    public NodesAreAlreadyConnected(String message) {
        super(message);
    }
}
