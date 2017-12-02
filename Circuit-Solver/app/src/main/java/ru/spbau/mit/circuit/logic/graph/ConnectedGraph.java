package ru.spbau.mit.circuit.logic.graph;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.system_solving.LinearSystem;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Monom;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Polynom;

public class ConnectedGraph {

    private final Vertex root;

    private int n = 0;
    private int m = 0;

    private Set<Vertex> vertices = new HashSet<>();
    private List<Edge> edges = new ArrayList<>();

    private List<Cycle> cycles = new ArrayList<>();

    private RealVector solution;
    private final List<Monom> variables = new ArrayList<>();
    private final List<Monom> constants = new ArrayList<>();

    ConnectedGraph(Vertex root) {
        this.root = root;
        n++;
    }

    public void solve() throws CircuitShortingException {
        findCycles();
        LinearSystem<Polynom, Polynom> system = constructSystem();
//        RealVector voltages = constructAnswer();

//        DecompositionSolver solver;
//        try {
//            solver = new LUDecomposition(system).getSolver();
//            solution = solver.solve(voltages);
//        } catch (SingularMatrixException e) {
//            throw new CircuitShortingException();
//        }
    }

    public void setCurrents() {
        Iterator<Edge> iterator = edges.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            iterator.next().setCurrent(solution.getEntry(i));
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

    private LinearSystem<Polynom, Polynom> constructSystem() {
        LinearSystem<Polynom, Polynom> system = new LinearSystem<>(m);
//        for ()
//        int index = 0;
        for (Node node : nodes) {
            system.addEquation(node.getEquation(variables, constants));
        }
        for (Cycle cycle : cycles) {
//            system.addEquation(cycle.getEquation());
//            system.setRowVector(index++, cycle.getEquation(m));
        }
//        for (int i = 0; i < m; i++) {
//            System.out.println(Arrays.toString(system.getRow(i)));
//        }
        return system;
    }

    private RealVector constructAnswer() {
        RealVector answer = new ArrayRealVector(m);
        Iterator<Cycle> iterator = cycles.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            answer.setEntry(i + n - 1, iterator.next().getVoltage());
        }
        return answer;
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
        variables.add(new Monom(edge.current()));
        constants.add(new Monom(edge.charge()));
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
        return sb.toString();
    }
}
