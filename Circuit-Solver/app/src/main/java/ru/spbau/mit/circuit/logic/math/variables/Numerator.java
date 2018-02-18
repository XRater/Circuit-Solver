package ru.spbau.mit.circuit.logic.math.variables;


/**
 * That class is used to numerate variables, therefore there is a strict order between them.
 */
public class Numerator {
    private static int id = 0;

    static int nextId() {
        if (id == Integer.MAX_VALUE) {
            refresh();
        }
        return id++;
    }

    public static void refresh() {
        id = 0;
    }
}
