package ru.spbau.mit.circuit.logic.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.elements.Item;
import ru.spbau.mit.circuit.model.elements.Wire;
import ru.spbau.mit.circuit.model.node.Node;

public class Graph {

    private Map<Node, VisitableVertex> vertices = new HashMap<>();

    public Graph(Model model) {

        int number = 0; //FOR DEBUG
        for (Node node : model.nodes()) {
            vertices.put(node, new VisitableVertex(number++));
        }
        for (Wire wire : model.wires()) {
            addNewEdge(vertices.get(wire.from()), vertices.get(wire.to()),
                    wire);
        }
        for (Element element : model.elements()) {
            addNewEdge(vertices.get(element.from()), vertices.get(element.to()),
                    element);
        }
    }

    public List<ConnectedGraph> decompose() {
        List<ConnectedGraph> graphs = new LinkedList<>();
        for (VisitableVertex node : vertices.values()) {
            if (!node.visited) {
                ConnectedGraph graph = addAdjacentEdges(new ConnectedGraph(node), node);
                graphs.add(graph);
                graph.addEdges();
            }
        }
        return graphs;
    }

    private void addNewEdge(Vertex u, Vertex v, Item item) {
        Edge e = new Edge(item, u, v);
        u.add(e);
        v.add(e);
    }

    private ConnectedGraph addAdjacentEdges(ConnectedGraph graph, VisitableVertex node) {
        node.visited = true;
        for (Edge edge : node.getEdges()) {
            VisitableVertex u = (VisitableVertex) edge.to();
            VisitableVertex v = (VisitableVertex) edge.from();
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

    private class VisitableVertex extends Vertex {
        private boolean visited;

        private VisitableVertex(int ID) {
            super(ID);
        }
    }

}
