package ru.spbau.mit.circuit.logic.graph;


import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ru.spbau.mit.circuit.logic.gauss.Equation;
import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.functions1.PolyFunction;
import ru.spbau.mit.circuit.logic.gauss.functions1.PolyFunctions;
import ru.spbau.mit.circuit.logic.gauss.linear_containers.Row;
import ru.spbau.mit.circuit.logic.gauss.linear_containers.Vector;
import ru.spbau.mit.circuit.logic.gauss.variables.Derivative;
import ru.spbau.mit.circuit.logic.gauss.variables.FunctionVariable;

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

    @SuppressWarnings("NullableProblems")
    @NonNull
    Iterable<Edge> getTreeEdges() {
        return this::treeEdgesIterator;
    }

    @SuppressWarnings("WeakerAccess")
    Iterator<Edge> treeEdgesIterator() {
        return new treeIterator();
    }


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
