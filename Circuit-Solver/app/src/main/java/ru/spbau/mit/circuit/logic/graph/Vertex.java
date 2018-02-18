package ru.spbau.mit.circuit.logic.graph;


import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ru.spbau.mit.circuit.logic.math.expressions.Expression;
import ru.spbau.mit.circuit.logic.math.expressions.Expressions;
import ru.spbau.mit.circuit.logic.math.linearContainers.FArray;
import ru.spbau.mit.circuit.logic.math.linearSystems.LSystem;
import ru.spbau.mit.circuit.logic.math.linearSystems.exceptions.InconsistentSystemException;

class Vertex {
    private final List<Edge> edges = new LinkedList<>();

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

    @SuppressWarnings("WeakerAccess")
    Iterator<Edge> treeEdgesIterator() {
        return new treeIterator();
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

    /**
     * Adds new equation corresponding to the first Kirchhoff's law.
     */
    void addEquation(LSystem<Expression, FArray<Expression>> system) throws
            InconsistentSystemException {
        FArray<Expression> coefficients = FArray.array(system.variablesNumber(), Expressions.zero
                ());
        for (Edge edge : edges) {
            coefficients.set(edge.index(), Expressions.constant(edge.getDirection(this)));
        }
        system.addEquation(coefficients,
                FArray.array(system.variablesNumber() + 1, Expressions.zero()));
    }

    @Override
    public String toString() {
        return String.valueOf("Vertex");
    }
}
