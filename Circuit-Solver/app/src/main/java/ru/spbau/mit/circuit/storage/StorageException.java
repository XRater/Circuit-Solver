package ru.spbau.mit.circuit.storage;


/**
 * An exception is thrown if an error occurred during working with DB. For example, poor internet
 * connection or a file can`t be read.
 */
public class StorageException extends Exception {
    StorageException(Throwable cause) {
        super(cause);
    }

    public StorageException() {
        super();
    }
}
