package ru.spbau.mit.circuit.storage;


/**
 * An exception is thrown if model didn`t load for any reason such as poor internet
 * connection or a file can`t be read.
 */
public class LoadingException extends Exception {
    LoadingException(Throwable cause) {
        super(cause);
    }
}
