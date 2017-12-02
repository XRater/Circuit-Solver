package ru.spbau.mit.circuit.logic.graph;


import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import ru.spbau.mit.circuit.logic.system_solving.Equation;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Monom;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Polynom;

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

    Equation<Polynom, Polynom> getEquation(List<Monom> variables, List<Monom> constants) {
        List<Monom> variablesCpy = new ArrayList<>();
        List<Monom> contantsCpy = new ArrayList<>();
        for (Monom m : variables) {
            variablesCpy.add(new Monom(m.variable()));
        }
        for (Monom m : constants) {
            contantsCpy.add(new Monom(m.variable()));
        }
        Polynom vars = new Polynom(variablesCpy);
        Polynom consts = new Polynom(contantsCpy);

        Vertex curr = edges.get(0).getAdjacent(edges.get(1));
        curr = edges.get(0).getPair(curr);
        for (Edge edge : edges) {
            vars.addMonom(new Monom(edge.current(), edge.getResistance() * edge.getDirection
                    (curr)));
            consts.addConst(edge.getVoltage() * edge.getDirection(curr));
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
