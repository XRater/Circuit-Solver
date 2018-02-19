package ru.spbau.mit.circuit.logic.graph;


import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.linearContainers.FArray;
import ru.spbau.mit.circuit.logic.math.linearSystems.LSystem;
import ru.spbau.mit.circuit.logic.math.linearSystems.exceptions.InconsistentSystemException;

class Vertex {
    private final List<Edge> edges = new LinkedList<>();

    @NonNull
    List<Edge> getEdges() {
        return edges;
    }

    void add(Edge e) {
        edges.add(e);
    }

    @SuppressWarnings("NullableProblems")
    @NonNull
    Iterable<Edge> getTreeEdges() {
        return this::treeEdgesIterator;
    }

    @NonNull
    @SuppressWarnings("WeakerAccess")
    Iterator<Edge> treeEdgesIterator() {
        return new treeIterator();
    }

    /**
     * Adds new equation corresponding to the first Kirchhoff's law.
     */
    void addEquation(@NonNull LSystem<Numerical, FArray<Numerical>> system) throws
            InconsistentSystemException {
        FArray<Numerical> coefficients = FArray.array(system.variablesNumber(), Numerical.zero());
        for (Edge edge : edges) {
            coefficients.set(edge.index(), Numerical.number(edge.getDirection(this)));
        }
        system.addEquation(coefficients, FArray.array(system.variablesNumber() + 1, Numerical
                .zero()));
    }

    @Override
    public String toString() {
        return String.valueOf("Vertex");
    }

    private class treeIterator implements Iterator<Edge> {


        Edge e;

        @NonNull
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
