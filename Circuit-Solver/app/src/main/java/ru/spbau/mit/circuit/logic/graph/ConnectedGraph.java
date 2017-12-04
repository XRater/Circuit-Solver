package ru.spbau.mit.circuit.logic.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.solver.Solver;
import ru.spbau.mit.circuit.logic.system_solving.LinearSystem;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.BoundedPolynom;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Monom;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Polynom;
import ru.spbau.mit.circuit.logic.system_solving.variables.Derivative;
import ru.spbau.mit.circuit.logic.system_solving.variables.FunctionVariable;
import ru.spbau.mit.circuit.logic.system_solving.variables.Numerator;

public class ConnectedGraph {

    private final Vertex root;

    private int n = 0;
    private int m = 0;

    private Set<Vertex> vertices = new HashSet<>();
    private List<Edge> edges = new ArrayList<>();

    private List<Cycle> cycles = new ArrayList<>();

    private final Polynom<Derivative> variables = new Polynom<>();

    ConnectedGraph(Vertex root) {
        Numerator.refresh();
        this.root = root;
        n++;
    }

    public void solve() throws CircuitShortingException {
        findCycles();
        LinearSystem<BoundedPolynom<Derivative>, Polynom<FunctionVariable>> system =
                constructSystem();
        Solver.solve(system);
    }

    public void setCurrents() {
        for (Edge edge : edges) {
            edge.updateCurrent();
        }
    }

    void add(Vertex u, Edge edge) {
        vertices.add(u);
        edge.addToTree();
        addEdge(edge);
        n++;
    }

    void addEdges() {
        for (Vertex vertex : vertices) {
            for (Edge edge : vertex.getEdges()) {
                if (edge.index() == -1) {
                    addEdge(edge);
                }
            }
        }
    }

    private void findCycles() {
        for (Edge edge : edges) {
            if (!edge.isInTree()) {
                Cycle cycle = getCycle(edge);
                cycles.add(cycle);
            }
        }
    }

    private LinearSystem<BoundedPolynom<Derivative>, Polynom<FunctionVariable>>
    constructSystem() {
        LinearSystem<BoundedPolynom<Derivative>, Polynom<FunctionVariable>> system = new
                LinearSystem<>(m);
        for (Vertex node : vertices) {
            System.out.println(node);
            system.addEquation(node.getEquation(variables));
        }
        for (Cycle cycle : cycles) {
            System.out.println(cycle);
            system.addEquation(cycle.getEquation(variables));
        }
        return system;
    }

    private Cycle getCycle(Edge edge) {
        Path path = new Path();
        findPath(path, edge.from(), edge.to());
        return new Cycle(path, edge);
    }

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

    private void addEdge(Edge edge) {
        variables.add(new Monom<>(edge.current()));
        edge.setIndex(edges.size());
        edges.add(edge);
        m++;
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
