package ru.spbau.mit.circuit.logic.graph;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ru.spbau.mit.circuit.logic.system_solving.Equation;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Monom;
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

    @NonNull
    Iterable<Edge> getTreeEdges() {
        return this::treeEdgesIterator;
    }

    @SuppressWarnings("WeakerAccess")
    Iterator<Edge> treeEdgesIterator() {
        return new treeIterator();
    }

    Equation<Polynom, Polynom> getEquation(List<Monom> variables, List<Monom> constants) {
        List<Monom> variablesCpy = new ArrayList<>();
        List<Monom> constantsCpy = new ArrayList<>();
        for (Monom m : variables) {
            variablesCpy.add(new Monom(m.variable()));
        }
        for (Monom m : constants) {
            constantsCpy.add(new Monom(m.variable()));
        }
        Polynom vars = new Polynom(variablesCpy);
        Polynom consts = new Polynom(constantsCpy);
        for (Edge edge : edges) {
            vars.addMonom(new Monom(edge.current(), edge.getDirection(this)));
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
