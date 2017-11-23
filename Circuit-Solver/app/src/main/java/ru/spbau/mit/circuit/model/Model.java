package ru.spbau.mit.circuit.model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.elements.Wire;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.model.node.Node;

public class Model {
    private List<Element> elements = new ArrayList<>();
    private List<Wire> wires = new ArrayList<>();
    private Set<Node> nodes = new HashSet<>();
    private Verificator verificator = new Verificator(this);

    public List<Element> elements() {
        return elements;
    }

    public List<Wire> wires() {
        return wires;
    }

    public Set<Node> nodes() {
        return nodes;
    }

    public void add(CircuitObject object) {
        if (object instanceof Node) {
            nodes.add((Node) object);
        } else if (object instanceof Element) {
            Element element = (Element) object;
            elements.add(element);
            nodes.add(element.from());
            nodes.add(element.to());
        } else if (object instanceof Wire) {
            Wire wire = (Wire) object;
            if (verificator.wireExists(wire)) {
                throw new InvalidCircuitObjectAddition("Wire ends already connected.");
            }
            if (!nodes.contains(wire.from()) || !nodes.contains(wire.to())) {
                throw new InvalidCircuitObjectAddition("One of ends is not under model control");
            }
            wires.add(wire);
            wire.from().addWire(wire);
            wire.to().addWire(wire);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean remove(CircuitObject object) {
        if (object instanceof Node) {
            return nodes.remove(object);
        } else if (object instanceof Element) {
            return elements.remove(object);
        } else if (object instanceof Wire) {
            return wires.remove(object);
        } else {
            throw new IllegalArgumentException();
        }
    }

    // Who should remove unused nodes from the set?
    public boolean removeWire(Wire wire) {
        return wires.remove(wire);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DrawableModel:\n");
        sb.append("Nodes:\n");
        for (Element e : elements) {
            sb.append(e).append("\n");
        }
        sb.append("Elements:\n");
        for (Element e : elements) {
            sb.append(e).append("\n");
        }
        sb.append("Wires:\n");
        for (Element e : elements) {
            sb.append(e).append("\n");
        }
        return sb.toString();
    }

    public void clear() {
        nodes.clear();
        elements.clear();
        wires.clear();
    }
}
