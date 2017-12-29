package ru.spbau.mit.circuit.logic.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.circuitObjects.Item;
import ru.spbau.mit.circuit.model.circuitObjects.elements.Element;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;
import ru.spbau.mit.circuit.model.circuitObjects.wires.Wire;

/**
 * Graph representation of model. May contain more then one component.
 */
public class Graph {

    // Vertexes of graph. Every vertex knows all adjacent edges.
    private Map<Node, VisitableVertex> vertices = new HashMap<>();

    /**
     * Constructs graph from the model object. Every element and wire will become edges,
     * nodes will become vertices.
     */
    public Graph(Model model) {
        // Store all model nodes.
        for (Node node : model.nodes()) {
            vertices.put(node, new VisitableVertex());
        }
        // Add edge for every wire.
        for (Wire wire : model.wires()) {
            addNewEdge(vertices.get(wire.from()), vertices.get(wire.to()),
                    wire);
        }
        // Add edge for every element.
        for (Element element : model.elements()) {
            addNewEdge(vertices.get(element.from()), vertices.get(element.to()),
                    element);
        }
    }

    // Creates new edge for the given item. Updates adjacent edges of its end vertices.
    private void addNewEdge(Vertex u, Vertex v, Item item) {
        Edge e = new Edge(item, u, v);
        u.add(e);
        v.add(e);
    }

    /**
     * The method decomposes the graph.
     *
     * @return list of connected subgraphs of the initial graph.
     */
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

    /**
     * Adds all neighbour vertices to the connected graph. This recursive function will build
     * connected component of the initial vertex.
     *
     * @param graph to update
     * @param node  node to work with
     * @return updated graph.
     */
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

    /**
     * Class to check if vertex was already visited.
     */
    private class VisitableVertex extends Vertex {
        private boolean visited;
    }

}
