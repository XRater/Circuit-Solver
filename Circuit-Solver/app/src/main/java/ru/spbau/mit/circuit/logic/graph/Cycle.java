package ru.spbau.mit.circuit.logic.graph;


import org.apache.commons.math3.util.BigReal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;

import ru.spbau.mit.circuit.logic.gauss.Equation;
import ru.spbau.mit.circuit.logic.gauss.functions1.PolyFunction;
import ru.spbau.mit.circuit.logic.gauss.functions1.PolyFunctions;
import ru.spbau.mit.circuit.logic.gauss.linear_containers.Row;
import ru.spbau.mit.circuit.logic.gauss.linear_containers.Vector;
import ru.spbau.mit.circuit.logic.gauss.variables.Derivative;
import ru.spbau.mit.circuit.logic.gauss.variables.FunctionVariable;

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


    Equation<
            BigReal,
            Vector<BigReal, Derivative>,
            Row<BigReal, FunctionVariable, PolyFunction>
            > getEquation(Collection<Derivative> variables) {

        Vector<BigReal, Derivative> vars = new Vector<>(variables, BigReal.ZERO);
        Row<BigReal, FunctionVariable, PolyFunction> consts =
                new Row<>(PolyFunctions.zero());

        Vertex curr = edges.get(0).getAdjacent(edges.get(1));
        curr = edges.get(0).getPair(curr);
        for (Edge edge : edges) {
            vars.add(edge.current(),
                    new BigReal(edge.getResistance() * edge.getDirection(curr)));

            if (edge.getCapacity() != 0) {
                consts.add(edge.charge(),
                        new BigReal(-edge.getDirection(curr) / edge.getCapacity()));
            }

            consts.addConst(PolyFunctions.constant(-edge.getVoltage() * edge.getDirection
                    (curr)));
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
