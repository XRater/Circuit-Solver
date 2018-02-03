package ru.spbau.mit.circuit.logic.graph;


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.linearContainers.Vector;
import ru.spbau.mit.circuit.logic.math.linearSystems.LinearSystem;
import ru.spbau.mit.circuit.logic.math.linearSystems.Row;
import ru.spbau.mit.circuit.logic.math.variables.Derivative;
import ru.spbau.mit.circuit.logic.math.variables.FunctionVariable;
import ru.spbau.mit.circuit.logic.math.variables.Numerator;
import ru.spbau.mit.circuit.logic.solver.Solver;

/**
 * Connected graph with known tree structure.
 */
public class ConnectedGraph {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Vertex root; // root of the graph

    @SuppressWarnings("unused")
    private int verticesNumber = 0; // number of vertices
    private int edgesNumber = 0; // number of edges

    private LinkedHashSet<Vertex> vertices = new LinkedHashSet<>();
    private List<Edge> edges = new ArrayList<>();

    private List<Cycle> cycles = new ArrayList<>(); // Base system of cycles

    private final Collection<Derivative> variables = new ArrayList<>();

    /**
     * Creates connected graph from the root. Graph will contain only one vertex and getZero edges.
     */
    ConnectedGraph(Vertex root) {
        Numerator.refresh();
        this.root = root;
        verticesNumber++;
    }

    /**
     * Adds new vertex and new edge. Edge must be adjacent to vertex, edge will be added to tree
     * structure.
     *
     * @param u    new vertex
     * @param edge new edge
     */
    void add(Vertex u, Edge edge) {
        vertices.add(u);
        edge.addToTree();
        addEdge(edge);
        verticesNumber++;
    }

    /**
     * Adds all edges adjacent to graph vertices to the graph. Added edges will not be in tree
     * structure.
     */
    void addEdges() {
        for (Vertex vertex : vertices) {
            for (Edge edge : vertex.getEdges()) {
                if (edge.index() == -1) {
                    addEdge(edge);
                }
            }
        }
    }


    /**
     * Adds one edge to the graph. Edge will not be in tree structure.
     * <p>
     * Edge must be not added to the graph yet (edge must have index equals to -1).
     *
     * @param edge edge to add.
     */
    private void addEdge(Edge edge) {
        if (edge.index() != -1) {
            throw new IllegalArgumentException();
        }
        variables.add(edge.current());
        edge.setIndex(edges.size());
        edges.add(edge);
        edgesNumber++;
    }

    /**
     * Finds charges and currents for every model item, and sets found values.
     *
     * @throws CircuitShortingException if circuit had cycle.
     */
    public void solve() throws CircuitShortingException {
        findCycles();
        LinearSystem<
                Numerical,
                Vector<Numerical, Derivative>,
                Row<Numerical, FunctionVariable>> system = constructSystem();
        Solver.solve(system);
        setCurrents();
    }

    private void setCurrents() {
        for (Edge edge : edges) {
            edge.updateCurrent();
        }
    }

    /**
     * The method finds base cycle system of the graph.
     */
    private void findCycles() {
        for (Edge edge : edges) {
            if (!edge.isInTree()) {
                Cycle cycle = getCycle(edge);
                cycles.add(cycle);
            }
        }
    }

    /**
     * The method constructs linear system of the graph, corresponding to the Om's laws.
     *
     * @return constructed linear system.
     */
    private LinearSystem<
            Numerical,
            Vector<Numerical, Derivative>,
            Row<Numerical, FunctionVariable>> constructSystem() {

        LinearSystem<Numerical,
                Vector<Numerical, Derivative>,
                Row<Numerical, FunctionVariable>> system =
                new LinearSystem<>(edgesNumber);

        for (Vertex node : vertices) {
            system.addEquation(node.getEquation(variables));
        }
        for (Cycle cycle : cycles) {
            system.addEquation(cycle.getEquation(variables));
        }

        return system;
    }

    /**
     * The method finds cycle through the given edge. Edge must be not in the tree structure.
     *
     * @param edge edge to find cycle through.
     */
    private Cycle getCycle(Edge edge) {
        if (edge.isInTree()) {
            throw new IllegalArgumentException();
        }
        Path path = new Path();
        findPath(path, edge.from(), edge.to());
        return new Cycle(path, edge);
    }

    /**
     * The method finds path from the begin vertex to the to vertex.
     * <p>
     * Found path will have edges only from tree structure. Path will be stored on path argument.
     *
     * @return true if path was found and false otherwise.
     */
    private boolean findPath(Path path, Vertex from, Vertex to) {
        if (from.equals(to)) {
            return true;
        }
        for (Edge edge : from.getTreeEdges()) {
            edge.removeFromTree();
            if (findPath(path, edge.getPair(from), to)) {
                path.push(edge);
                edge.addToTree();
                return true;
            }
            edge.addToTree();
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Edge edge : edges) {
            sb.append(edge.toString()).append("\n");
        }
        sb.append("Variables ").append(variables.toString()).append("\n");
        return sb.toString();
    }
}
