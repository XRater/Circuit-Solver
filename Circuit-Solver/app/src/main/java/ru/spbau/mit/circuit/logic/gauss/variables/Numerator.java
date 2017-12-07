package ru.spbau.mit.circuit.logic.gauss.variables;


public class Numerator {
    private static int id = 0;

    public static void refresh() {
        id = 0;
    }

    public static int nextId() {
        if (id == Integer.MAX_VALUE) {
            refresh();
        }
        return id++;
    }
}
