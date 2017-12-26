package ru.spbau.mit.circuit.model;


import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import ru.spbau.mit.circuit.model.elements.Element;
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
        Set<Node> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            System.out.println(node);
            if (visited.contains(node)) {
                continue;
            }
            visited.add(node);
            if (node == to) {
                return true;
            }
            for (Wire wire : node.wires()) {
                System.out.println(wire);
                queue.add(wire.opposite(node));
            }
        }
        return false;
    }

    public boolean wireExists(Wire wire) {
        return oneWire(wire.from(), wire.to());
    }

    public boolean isolated(Node node) {
        for (Element element : model.elements()) {
            if (element.adjacent(node)) {
                return false;
            }
        }
        for (Wire wire : model.wires()) {
            if (wire.adjacent(node)) {
                return false;
            }
        }
        return true;
    }

    public Node findUnnecessaryNode() {
        for (Node node : model.nodes()) {
            if (node.wires().size() != 2) {
                continue;
            }
            boolean elementEnd = false;
            for (Element element : model.elements()) {
                if (element.adjacent(node)) {
                    elementEnd = true;
                }
            }
            if (!elementEnd) {
                return node;
            }
        }
        return null;
    }

    public boolean verify() {
        if (findUnnecessaryNode() != null) {
            return false;
        }
        return true;
    }
}
