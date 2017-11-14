package ru.spbau.mit.circuit.logic.graph;


import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.Deque;

class Cycle {

    private final ArrayList<Edge> edges = new ArrayList<>();

    Cycle(Path path, Edge e) {
        Deque<Edge> pathEdges = path.edges();
        if (!pathEdges.getLast().adjacent(e) ||
                !pathEdges.getFirst().adjacent(e)) {
            throw new IllegalArgumentException();
        }

        edges.addAll(pathEdges);
        edges.add(e);
    }

    double getVoltage() {
        double voltage = 0;
        Node curr = edges.get(0).getAdjacent(edges.get(1));
        curr = edges.get(0).getPair(curr);
        for (Edge edge : edges) {
            voltage += edge.getVoltage();
            curr = edge.getPair(curr);
        }
        return voltage;
    }

    RealVector getEquation(int m) {
        RealVector equation = new ArrayRealVector(m);
        Node curr = edges.get(0).getAdjacent(edges.get(1));
        curr = edges.get(0).getPair(curr);
        for (Edge edge : edges) {
            equation.setEntry(edge.index(), edge.getResistance() * edge.getDirection(curr));
            curr = edge.getPair(curr);
        }
        return equation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Edge e : edges) {
            sb.append(e).append("--");
        }
        return sb.toString();
    }
}
