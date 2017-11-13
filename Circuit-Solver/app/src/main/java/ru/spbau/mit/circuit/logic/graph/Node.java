package ru.spbau.mit.circuit.logic.graph;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class Node {
    private final List<Edge> edges = new LinkedList<>();

    public List<Edge> getEdges() {
        return edges;
    }

    public void add(Edge e) {
        edges.add(e);
    }

    public Iterable<Edge> getTreeEdges() {
        return this::treeEdgesIterator;
    }

    public Iterator<Edge> treeEdgesIterator() {
        return new treeIterator();
    }

    private class treeIterator implements Iterator<Edge> {

        private Iterator<Edge> iterator = edges.iterator();

        @Override
        public boolean hasNext() {
            Edge e;
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
            Edge e;
            while (iterator.hasNext()) {
                e = iterator.next();
                if (e.isInTree()) {
                    return e;
                }
            }
            return null;
        }
    }
}
