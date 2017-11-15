package ru.spbau.mit.circuit.ui;


import java.util.HashMap;
import java.util.Map;

import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;

public class NodeController {

    private static final int cellSize = Drawer.CELL_SIZE;
    private static final int fieldSize = Drawer.FIELD_SIZE;

    private static final Map<Node, Drawable> nodes = new HashMap<>();

    public static Map<Node, Drawable> nodes() {
        return nodes;
    }

    public static Node getNode() {
        return null;
    }
}
