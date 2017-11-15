package ru.spbau.mit.circuit.logic.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ru.spbau.mit.circuit.model.Elements.Element;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.Point;

public class Graph {

    private HashMap<Point, VisitableNode> nodes = new HashMap<>();

    public Graph(Model model) {

        int number = 0; //FOR DEBUG
        for (Element element : model.getElements()) {
            if (!nodes.containsKey(element.getFrom())) {
                System.out.println(element.getFrom().toString() + " " + number);
                VisitableNode node = new VisitableNode(number++);
                nodes.put(element.getFrom(), node);
            }
            if (!nodes.containsKey(element.getTo())) {
                System.out.println(element.getTo().toString() + " " + number);
                VisitableNode node = new VisitableNode(number++);
                nodes.put(element.getTo(), node);
            }
        }

        for (Element element : model.getElements()) {
            addNewEdge(nodes.get(element.getFrom()), nodes.get(element.getTo()), element);
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

    private void addNewEdge(Node u, Node v, Element element) {
        Edge e = new Edge(element, u, v);
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
