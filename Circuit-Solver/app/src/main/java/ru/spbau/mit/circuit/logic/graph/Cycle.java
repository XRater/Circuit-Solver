package ru.spbau.mit.circuit.logic.graph;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;

import ru.spbau.mit.circuit.logic.system_solving.Equation;
import ru.spbau.mit.circuit.logic.system_solving.functions.FunctionExpression;
import ru.spbau.mit.circuit.logic.system_solving.functions.Zero;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Row;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Vector;
import ru.spbau.mit.circuit.logic.system_solving.variables.Derivative;
import ru.spbau.mit.circuit.logic.system_solving.variables.FunctionVariable;

class Cycle {

    private final ArrayList<Edge> edges = new ArrayList<>();

    Cycle(Path path, Edge e) {
        Deque<Edge> pathEdges = path.edges();
        if (!pathEdges.getLast().adjacent(e) ||
                !pathEdges.getFirst().adjacent(e)) {
            throw new IllegalArgumentException();
        }

        edges.addAll(pathEdges);
        edges.add(e);
    }

    Equation<Row<Derivative>, Vector<FunctionVariable, FunctionExpression>> getEquation(
            Collection<Derivative> variables) {

        Row<Derivative> vars = new Row<>(variables);
        Vector<FunctionVariable, FunctionExpression> consts = new Vector<>(new Zero());

        Vertex curr = edges.get(0).getAdjacent(edges.get(1));
        curr = edges.get(0).getPair(curr);
        for (Edge edge : edges) {
            vars.add(edge.current(), edge.getResistance() * edge.getDirection(curr));
            if (edge.getCapacity() != 0) {
                consts.add(edge.charge(), edge.getDirection(curr) / edge.getCapacity());
            }
            consts.addConst(new Zero(edge.getVoltage() * edge.getDirection(curr)));
            curr = edge.getPair(curr);
        }
        return new Equation<>(vars, consts);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Edge e : edges) {
            sb.append(e).append("--");
        }
        return sb.toString();
    }
}
