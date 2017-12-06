package ru.spbau.mit.circuit.logic.graph;


import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ru.spbau.mit.circuit.logic.system_solving.Equation;
import ru.spbau.mit.circuit.logic.system_solving.functions.FunctionExpression;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Row;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Vector;
import ru.spbau.mit.circuit.logic.system_solving.variables.Derivative;
import ru.spbau.mit.circuit.logic.system_solving.variables.FunctionVariable;

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

    Equation<Row<Derivative>, Vector<FunctionVariable, FunctionExpression>> getEquation(
            Collection<Derivative> variables) {

        System.err.println("Vertex:");
        Row<Derivative> vars = new Row<>(variables);
        for (FunctionVariable variable : variables) {
            System.err.println(variable.id());
        }
        Vector<FunctionVariable, FunctionExpression> consts = new Vector<>(FunctionExpression
                .empty());
        for (Edge edge : edges) {
            System.err.println(edge.current().id());
            vars.add(edge.current(), edge.getDirection(this));
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
