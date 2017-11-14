package ru.spbau.mit.circuit.logic.graph;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ConnectedGraph {

    private final Node root;

    private RealMatrix system;

    private Set<Node> nodes = new HashSet<>();
    private ArrayList<Edge> edges = new ArrayList<>();

    public ConnectedGraph(Node root) {
        this.root = root;
    }

    public void add(Node u, Edge edge) {
        nodes.add(u);
        edge.addToTree();
        addEdge(edge);
    }

    public void addEdges() {
        for (Node node : nodes) {
            for (Edge edge : node.getEdges()) {
                if (edge.index() == -1) {
                    addEdge(edge);
                }
            }
        }
    }

//    private void

    private void addEdge(Edge edge) {
        edge.setIndex(edges.size());
        edges.add(edge);
        System.out.println(edge);
    }

    public void solve() {

    }
}
