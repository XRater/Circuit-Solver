package ru.spbau.mit.circuit.logic.system_solving.variables;


public class Numerator {
    private static int id = 0;

    public static void refresh() {
        id = 0;
    }

    public static int nextId() {
        return id++;
    }
}
