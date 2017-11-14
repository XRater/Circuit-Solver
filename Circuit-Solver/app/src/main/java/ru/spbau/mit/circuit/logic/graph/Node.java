package ru.spbau.mit.circuit.logic.graph;


import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class Node {
    private final List<Edge> edges = new LinkedList<>();
    private int ID; //FOR DEBUG

    Node(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return String.valueOf(ID);
    }

    List<Edge> getEdges() {
        return edges;
    }

    public void add(Edge e) {
        edges.add(e);
    }

    Iterable<Edge> getTreeEdges() {
        return this::treeEdgesIterator;
    }

    Iterator<Edge> treeEdgesIterator() {
        return new treeIterator();
    }

    RealVector getEquation(int size) {
        RealVector equation = new ArrayRealVector(size);
        for (Edge edge : edges) {
            equation.setEntry(edge.index(), edge.getDirection(this));
        }
        return equation;
    }

    private class treeIterator implements Iterator<Edge> {

        Edge e;
        private Iterator<Edge> iterator = edges.iterator();

        @Override
        public boolean hasNext() {
            while (iterator.hasNext()) {
                e = iterator.next();
                if (e.isInTree()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Edge next() {
            while (e != null && !e.isInTree()) {
                e = iterator.next();
            }
            return e;
        }
    }
}
