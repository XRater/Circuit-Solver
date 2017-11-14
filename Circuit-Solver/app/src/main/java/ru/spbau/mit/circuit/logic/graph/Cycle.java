package ru.spbau.mit.circuit.logic.graph;


import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.Deque;
import java.util.LinkedList;

class Cycle {

    private final Deque<Edge> edges = new LinkedList<>();

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
        Node curr = edges.getFirst().to();
        for (Edge edge : edges) {
            voltage += edge.getVoltage();
            curr = edge.getPair(curr);
        }
        if (!curr.equals(edges.getFirst().to())) {
            throw new IllegalArgumentException();
        }
        return voltage;
    }

    RealVector getEquation(int m) {
        RealVector equation = new ArrayRealVector(m);
        Node curr = edges.getFirst().to();
        for (Edge edge : edges) {
            equation.setEntry(edge.index(), edge.getResistance() * edge.getDirection(curr));
            curr = edge.getPair(curr);
        }
        if (!curr.equals(edges.getFirst().to())) {
            throw new IllegalArgumentException();
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
