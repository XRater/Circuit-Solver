package ru.spbau.mit.circuit.model;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import ru.spbau.mit.circuit.model.circuitObjects.elements.Element;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;
import ru.spbau.mit.circuit.model.circuitObjects.wires.Wire;


/**
 * This class is used to verify model invariants, such as "no more the one wire between two nodes"
 * and "every node degree must be at least 3".
 */
class Verificator {

    private Model model; // model to verify

    Verificator(Model model) {
        this.model = model;
    }

    /**
     * Checking if wire connects two nodes, which are already connected.
     *
     * @param wire a wire to check.
     * @return true if end-nodes are already connected and false otherwise.
     */
    boolean wireExists(@NonNull Wire wire) {
        return oneWire(wire.from(), wire.to());
    }

    /**
     * The method checks if node has no adjacent to it wires and elements, thus it can be deleted.
     *
     * @param node node to check
     * @return true if node is isolated and false otherwise.
     */
    boolean isIsolated(@NonNull Node node) {
        for (Element element : model.elements()) {
            if (element.adjacent(node)) {
                return false;
            }
        }
        return node.wires().size() == 0;
    }

    /**
     * The method finds node with wire degree less then three.
     *
     * @return node with wire degree equals to two and null if there was not any.
     */
    @Nullable
    Node findUnnecessaryNode() {
        for (Node node : model.nodes()) {
            if (node.wires().size() > 2) {
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

    private boolean oneWire(Node from, Node to) {
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(from);
        Set<Node> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (visited.contains(node)) {
                continue;
            }
            visited.add(node);
            if (node == to) {
                return true;
            }
            for (Wire wire : node.wires()) {
                queue.add(wire.opposite(node));
            }
        }
        return false;
    }
}
