package ru.spbau.mit.circuit.model;


import java.util.ArrayDeque;
import java.util.Queue;

import ru.spbau.mit.circuit.model.elements.Wire;
import ru.spbau.mit.circuit.model.node.Node;

public class Verificator {
    private Model model;

    Verificator(Model model) {
        this.model = model;
    }

    public boolean oneWire(Node from, Node to) {
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(from);
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node == to) {
                return true;
            }
            for (Wire wire : node.wires()) {
                queue.add(wire.opposite(node));
            }
        }
        return false;
    }

    public boolean wireExists(Wire wire) {
        return oneWire(wire.from(), wire.to());
    }
}
