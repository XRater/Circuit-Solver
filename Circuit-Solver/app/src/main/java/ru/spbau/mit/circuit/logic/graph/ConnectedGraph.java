package ru.spbau.mit.circuit.logic.graph;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConnectedGraph {

    private final Node root;

    private int n = 0;
    private int m = 0;

    private Set<Node> nodes = new HashSet<>();
    private ArrayList<Edge> edges = new ArrayList<>();

    private ArrayList<Cycle> cycles = new ArrayList<>();

    private RealVector solution;

    ConnectedGraph(Node root) {
        this.root = root;
        n++;
    }

    public void solve() {
        findCycles();
        RealMatrix system = constructSystem();
        RealVector voltages = constructAnswer();
        System.out.println(voltages);

        DecompositionSolver solver = new LUDecomposition(system).getSolver();
        solution = solver.solve(voltages);

        System.out.println(solution);
    }

    public void setCurrents() {
        for (int i = 0; i < edges.size(); i++) {
            edges.get(i).setCurrent(solution.getEntry(i));
        }
    }

    void add(Node u, Edge edge) {
        nodes.add(u);
        edge.addToTree();
        addEdge(edge);
        n++;
    }

    void addEdges() {
        for (Node node : nodes) {
            for (Edge edge : node.getEdges()) {
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
                System.out.println(cycle);
                cycles.add(cycle);
            }
        }
    }

    private RealMatrix constructSystem() {
        RealMatrix system = new Array2DRowRealMatrix(m, m);
        int index = 0;
        for (Node node : nodes) {
            system.setRowVector(index++, node.getEquation(m));
        }
        for (Cycle cycle : cycles) {
            system.setRowVector(index++, cycle.getEquation(m));
        }
        for (int i = 0; i < m; i++) {
            System.out.println(Arrays.toString(system.getRow(i)));
        }
        return system;
    }

    private RealVector constructAnswer() {
        RealVector answer = new ArrayRealVector(m);
        for (int i = 0; i <= m - n; i++) {
            answer.setEntry(i + n - 1, cycles.get(i).getVoltage());
        }
        return answer;
    }

    private Cycle getCycle(Edge edge) {
        Path path = new Path();
        findPath(path, edge.from(), edge.to());
        return new Cycle(path, edge);
    }

    private boolean findPath(Path path, Node from, Node to) {
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
