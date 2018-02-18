package ru.spbau.mit.circuit.logic.graph;

import java.util.ArrayList;
import java.util.Deque;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.linearContainers.FArray;
import ru.spbau.mit.circuit.logic.math.linearSystems.LSystem;
import ru.spbau.mit.circuit.logic.math.linearSystems.exceptions.InconsistentSystemException;

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


    void addEquation(LSystem<Numerical, FArray<Numerical>> system) throws
            InconsistentSystemException {

        FArray<Numerical> coefficients = FArray.array(system.variablesNumber(), Numerical.zero());
        FArray<Numerical> constant = FArray.array(system.variablesNumber() + 1, Numerical.zero());
        Numerical voltage = Numerical.zero();

        Vertex curr = edges.get(0).getAdjacent(edges.get(1));
        curr = edges.get(0).getPair(curr);
        for (Edge edge : edges) {
            coefficients.set(edge.index(),
                    Numerical.number(edge.getResistance() * edge.getDirection(curr)));

            if (edge.getCapacity() != 0) {
                constant.set(edge.index(),
                        Numerical.number(-edge.getDirection(curr) / edge.getCapacity()));
            }

            voltage = voltage.add(Numerical.number(-edge.getVoltage() * edge.getDirection(curr)));
            curr = edge.getPair(curr);
        }

        constant.set(system.variablesNumber(), voltage);

        try {
            system.addEquation(coefficients, constant);
        } catch (InconsistentSystemException e) {
            for (int i = 0; i < system.variablesNumber(); i++) {
                if (!coefficients.get(i).isZero()) {
                    throw new RuntimeException(); // should never happen
                }
            }
            if (!constant.get(system.variablesNumber()).isZero()) {
                throw new InconsistentSystemException();
            }
            for (int i = 0; i < system.variablesNumber(); i++) {
                coefficients.set(i, constant.get(i));
            }
            system.addEquation(coefficients,
                    FArray.array(system.variablesNumber() + 1, Numerical.zero()));
        }
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
