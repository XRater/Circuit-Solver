package ru.spbau.mit.circuit.logic.graph;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.functions.Function;
import ru.spbau.mit.circuit.logic.math.linearContainers.FArray;
import ru.spbau.mit.circuit.logic.math.linearSystems.LinearSystem;
import ru.spbau.mit.circuit.logic.math.linearSystems.exceptions.InconsistentSystemException;
import ru.spbau.mit.circuit.logic.math.variables.Derivative;
import ru.spbau.mit.circuit.logic.math.variables.Numerator;
import ru.spbau.mit.circuit.logic.solver.Solver;

/**
 * Connected graph with known tree structure.
 */
public class ConnectedGraph {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Vertex root; // root of the graph
    private final Collection<Derivative> variables = new ArrayList<>();
    @SuppressWarnings("unused")
    private int verticesNumber = 0; // number of vertices
    @SuppressWarnings("unused")
    private int edgesNumber = 0; // number of edges
    @NonNull
    private LinkedHashSet<Vertex> vertices = new LinkedHashSet<>();
    @NonNull
    private List<Edge> edges = new ArrayList<>();
    @NonNull
    private List<Cycle> cycles = new ArrayList<>(); // Base system of cycles

    /**
     * Creates connected graph from the root. Graph will contain only one vertex and zero edges.
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
    void add(Vertex u, @NonNull Edge edge) {
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
    private void addEdge(@NonNull Edge edge) {
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
        LinearSystem<Numerical, FArray<Numerical>> system;
        try {
            system = constructSystem();
        } catch (InconsistentSystemException e) {
            throw new CircuitShortingException();
        }
        ArrayList<Function> solution = Solver.solve(system);
        setCurrents(solution);
    }

    private void setCurrents(@NonNull ArrayList<Function> solution) {
        for (Edge edge : edges) {
            edge.charge().setValue(solution.get(edge.index()));
            edge.current().setValue();
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
    @NonNull
    private LSystem<
    private LinearSystem<
            Numerical,
            FArray<Numerical>> constructSystem() throws InconsistentSystemException {

        LinearSystem<Numerical, FArray<Numerical>> system = new LinearSystem<>(
                variables.size(), Numerical.zero(), FArray.array(variables.size() + 1, Numerical
                .zero()));

        for (Vertex node : vertices) {
            node.addEquation(system);
        }
        for (Cycle cycle : cycles) {
            cycle.addEquation(system);
        }

        return system;
    }

    /**
     * The method finds cycle through the given edge. Edge must be not in the tree structure.
     *
     * @param edge edge to find cycle through.
     */
    private Cycle getCycle(@NonNull Edge edge) {
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
    private boolean findPath(@NonNull Path path, @NonNull Vertex from, Vertex to) {
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

    @NonNull
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