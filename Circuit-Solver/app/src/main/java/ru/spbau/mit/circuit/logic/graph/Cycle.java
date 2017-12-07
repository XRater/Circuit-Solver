package ru.spbau.mit.circuit.logic.graph;


import java.util.ArrayList;
import java.util.Deque;

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

    /*
        Equation<Vector<Derivative>, Row<FunctionVariable, FunctionExpression>> getEquation(
                Collection<Derivative> variables) {

            Vector<Derivative> vars = new Vector<>(variables);
            Row<FunctionVariable, FunctionExpression> consts = new Row<>(FunctionExpression
                    .empty());

            Vertex curr = edges.get(0).getAdjacent(edges.get(1));
            curr = edges.get(0).getPair(curr);
            for (Edge edge : edges) {
                vars.add(edge.current(), edge.getResistance() * edge.getDirection(curr));
                if (edge.getCapacity() != 0) {
                    consts.add(edge.charge(), edge.getDirection(curr) / edge.getCapacity());
                }
                consts.addConst(FunctionExpression.constant(edge.getVoltage() * edge.getDirection
                        (curr)));
                curr = edge.getPair(curr);
            }
            return new Equation<>(vars, consts);
        }
    */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Edge e : edges) {
            sb.append(e).append("--");
        }
        return sb.toString();
    }
}
