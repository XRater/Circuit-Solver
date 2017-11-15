package ru.spbau.mit.circuit.logic.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.elements.CircuitItem;
import ru.spbau.mit.circuit.model.point.Point;

public class Graph {

    private HashMap<Point, VisitableNode> nodes = new HashMap<>();

    public Graph(Model model) {

        int number = 0; //FOR DEBUG
        for (CircuitItem circuitItem : model.getCircuitItems()) {
            if (!nodes.containsKey(circuitItem.getFrom())) {
                System.out.println(circuitItem.getFrom().toString() + " " + number);
                VisitableNode node = new VisitableNode(number++);
                nodes.put(circuitItem.getFrom(), node);
            }
            if (!nodes.containsKey(circuitItem.getTo())) {
                System.out.println(circuitItem.getTo().toString() + " " + number);
                VisitableNode node = new VisitableNode(number++);
                nodes.put(circuitItem.getTo(), node);
            }
        }

        for (CircuitItem circuitItem : model.getCircuitItems()) {
            addNewEdge(nodes.get(circuitItem.getFrom()), nodes.get(circuitItem.getTo()),
                    circuitItem);
        }
    }

    public List<ConnectedGraph> decompose() {
        List<ConnectedGraph> graphs = new LinkedList<>();
        for (VisitableNode node : nodes.values()) {
            if (!node.visited) {
                ConnectedGraph graph = addAdjacentEdges(new ConnectedGraph(node), node);
                graphs.add(graph);
                graph.addEdges();
            }
        }
        return graphs;
    }

    private void addNewEdge(Node u, Node v, CircuitItem circuitItem) {
        Edge e = new Edge(circuitItem, u, v);
        u.add(e);
        v.add(e);
    }

    private ConnectedGraph addAdjacentEdges(ConnectedGraph graph, VisitableNode node) {
        node.visited = true;
        for (Edge edge : node.getEdges()) {
            VisitableNode u = (VisitableNode) edge.to();
            VisitableNode v = (VisitableNode) edge.from();
            if (!u.visited) {
                graph.add(u, edge);
                addAdjacentEdges(graph, u);
            }
            if (!v.visited) {
                graph.add(v, edge);
                addAdjacentEdges(graph, v);
            }
        }
        return graph;
    }

    private class VisitableNode extends Node {
        private boolean visited;

        private VisitableNode(int ID) {
            super(ID);
        }
    }

}
