package ru.spbau.mit.circuit.logic.graph;


import java.util.ArrayList;
import java.util.Deque;

import ru.spbau.mit.circuit.logic.system_solving.Equation;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.BoundedPolynom;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Monom;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Polynom;
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

    Equation<BoundedPolynom<Derivative>, Polynom<FunctionVariable>> getEquation(
            Polynom<Derivative> variables) {

        BoundedPolynom<Derivative> vars = new BoundedPolynom<>(variables);
        Polynom<FunctionVariable> consts = new Polynom<>();

        Vertex curr = edges.get(0).getAdjacent(edges.get(1));
        curr = edges.get(0).getPair(curr);
        for (Edge edge : edges) {
            vars.addMonom(new Monom<>(edge.current(), edge.getResistance() * edge.getDirection
                    (curr)));
            if (edge.getCapacity() != 0) {
                consts.add(new Monom<>(edge.charge(), edge.getDirection(curr) / edge
                        .getCapacity()));
            }
//            consts.addConst(edge.getVoltage() * edge.getDirection(curr));
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
