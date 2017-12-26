package ru.spbau.mit.circuit.logic.graph;


import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.functions.PolyFunction;
import ru.spbau.mit.circuit.logic.math.functions.PolyFunctions;
import ru.spbau.mit.circuit.logic.math.linearContainers.Vector;
import ru.spbau.mit.circuit.logic.math.linearSystems.Equation;
import ru.spbau.mit.circuit.logic.math.linearSystems.Row;
import ru.spbau.mit.circuit.logic.math.variables.Derivative;
import ru.spbau.mit.circuit.logic.math.variables.FunctionVariable;

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
     * Makes new equation corresponding to the first Oms law.
     */
    Equation<
            Numerical,
            Vector<Numerical, Derivative>,
            Row<Numerical, FunctionVariable, PolyFunction>
            > getEquation(Collection<Derivative> variables) {

        Vector<Numerical, Derivative> vars = new Vector<>(variables, Numerical.zero());
        Row<Numerical, FunctionVariable, PolyFunction> consts =
                new Row<>(PolyFunctions.zero());

        for (Edge edge : edges) {
            vars.add(edge.current(), Numerical.number(edge.getDirection(this)));
        }
        return new Equation<>(vars, consts);
    }

    @Override
    public String toString() {
        return String.valueOf("Vertex");
    }
}
