package ru.spbau.mit.circuit.model.point;


public class InvalidPointException extends RuntimeException {

    public InvalidPointException() {
    }

    public InvalidPointException(String message) {
        super(message);
    }

}
