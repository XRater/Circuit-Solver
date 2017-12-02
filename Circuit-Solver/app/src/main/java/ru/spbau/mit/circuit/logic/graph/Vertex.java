package ru.spbau.mit.circuit.logic.graph;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ru.spbau.mit.circuit.logic.system_solving.Equation;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Polynom;

class Vertex {
    private final List<Edge> edges = new LinkedList<>();
    private int ID; //FOR DEBUG

    Vertex(int ID) {
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

    Equation<Polynom, Polynom> getEquation(List<Monom> variables, List<Monom> constants) {
        Polynom vars = new Polynom(variables);
        Polynom consts = new Polynom(constants);
        for (Edge edge : edges) {

            //            equation.setEntry(edge.index(), edge.getDirection(this));
        }
        return new Equation<>(vars, consts);
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
