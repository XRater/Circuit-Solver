package ru.spbau.mit.circuit.logic.graph;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.functions.PolyFunction;
import ru.spbau.mit.circuit.logic.math.functions.PolyFunctions;
import ru.spbau.mit.circuit.logic.math.linearContainers.Vector;
import ru.spbau.mit.circuit.logic.math.linearSystems.Equation;
import ru.spbau.mit.circuit.logic.math.linearSystems.Row;
import ru.spbau.mit.circuit.logic.math.variables.Derivative;
import ru.spbau.mit.circuit.logic.math.variables.FunctionVariable;

class Cycle {

    private final ArrayList<Edge> edges = new ArrayList<>();

    Cycle(@NonNull Path path, @NonNull Edge e) {
        Deque<Edge> pathEdges = path.edges();
        if (!pathEdges.getLast().adjacent(e) ||
                !pathEdges.getFirst().adjacent(e)) {
            throw new IllegalArgumentException();
        }

        edges.addAll(pathEdges);
        edges.add(e);
    }


    @NonNull
    Equation<
            Numerical,
            Vector<Numerical, Derivative>,
            Row<Numerical, FunctionVariable, PolyFunction>
            > getEquation(@NonNull Collection<Derivative> variables) {

        Vector<Numerical, Derivative> vars = new Vector<>(variables, Numerical.zero());
        Row<Numerical, FunctionVariable, PolyFunction> consts =
                new Row<>(PolyFunctions.zero());

        Vertex curr = edges.get(0).getAdjacent(edges.get(1));
        curr = edges.get(0).getPair(curr);
        for (Edge edge : edges) {
            vars.add(edge.current(),
                    Numerical.number(edge.getResistance() * edge.getDirection(curr)));

            if (edge.getCapacity() != 0) {
                consts.add(edge.charge(),
                        Numerical.number(-edge.getDirection(curr) / edge.getCapacity()));
            }

            consts.addConst(PolyFunctions.constant(-edge.getVoltage() * edge.getDirection
                    (curr)));
            curr = edge.getPair(curr);
        }
        return new Equation<>(vars, consts);
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Edge e : edges) {
            sb.append(e).append("--");
        }
        return sb.toString();
    }
}
