package ru.spbau.mit.circuit.logic.graph;

import java.util.HashSet;
import java.util.Set;

public class ConnectedGraph {

    private final Node root;

    private Set<Node> nodes = new HashSet<>();

    public ConnectedGraph(Node root) {
        this.root = root;
    }

    public void add(Node u, Edge edge) {
        nodes.add(u);
        edge.addToTree();
    }

    public void foo() {
        for (Edge e : root.getTreeEdges()) {

        }
    }
}
