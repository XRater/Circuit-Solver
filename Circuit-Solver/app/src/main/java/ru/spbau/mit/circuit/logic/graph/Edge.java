package ru.spbau.mit.circuit.logic.graph;


import ru.spbau.mit.circuit.model.Battery;
import ru.spbau.mit.circuit.model.Element;
import ru.spbau.mit.circuit.model.Resistor;

class Edge {

    private final Element element;
    private final Node u;
    private final Node v;
    public int ID; //FOR TESTS
    private boolean inTree;

    Edge(Element element, Node u, Node v) {
        this.element = element;
        this.u = u;
        this.v = v;
    }

    public double getVoltage() {
        if (element instanceof Battery) {
            Battery battery = (Battery) element;
            return battery.getVoltage();
        }
        return 0;
    }

    public double getResistance() {
        if (element instanceof Resistor) {
            Resistor resistor = (Resistor) element;
            return resistor.getResistance();
        }
        return 0;
    }

    public Node v() {
        return v;
    }

    public Node u() {
        return u;
    }

    public void addToTree() {
        inTree = true;
    }

    public boolean isInTree() {
        return inTree;
    }
}
