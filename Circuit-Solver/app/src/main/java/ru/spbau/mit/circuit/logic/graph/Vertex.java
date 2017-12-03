package ru.spbau.mit.circuit.logic.graph;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ru.spbau.mit.circuit.logic.system_solving.Equation;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Monom;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Polynom;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.VarMonom;
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

    @NonNull
    Iterable<Edge> getTreeEdges() {
        return this::treeEdgesIterator;
    }

    @SuppressWarnings("WeakerAccess")
    Iterator<Edge> treeEdgesIterator() {
        return new treeIterator();
    }

    Equation<Polynom<FunctionVariable>, Polynom<FunctionVariable>> getEquation(
            List<Monom<FunctionVariable>> variables, List<Monom<FunctionVariable>> constants) {

        List<Monom<FunctionVariable>> variablesCpy = new ArrayList<>();
        List<Monom<FunctionVariable>> constantsCpy = new ArrayList<>();
        for (Monom<FunctionVariable> m : variables) {
            variablesCpy.add(new VarMonom(m.value()));
        }
        for (Monom<FunctionVariable> m : constants) {
            constantsCpy.add(new VarMonom(m.value()));
        }
        Polynom<FunctionVariable> vars = new Polynom<>(variablesCpy);
        Polynom<FunctionVariable> consts = new Polynom<>(constantsCpy);
        for (Edge edge : edges) {
            vars.addMonom(new VarMonom(edge.current(), edge.getDirection(this)));
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
