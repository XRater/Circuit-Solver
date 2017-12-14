package ru.spbau.mit.circuit.model;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.elements.Wire;
import ru.spbau.mit.circuit.model.exceptions.InvalidCircuitObjectAddition;
import ru.spbau.mit.circuit.model.exceptions.InvalidCircuitObjectDeletion;
import ru.spbau.mit.circuit.model.exceptions.NodesAreAlreadyConnected;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.model.node.Node;

public class Model {
    private Controller controller;
    private List<Element> elements = new ArrayList<>();
    private List<Wire> wires = new ArrayList<>();
    private Set<Node> nodes = new HashSet<>();

    private Verificator verificator = new Verificator(this);

    public Model(Controller controller) {
        this.controller = controller;
    }

    public List<Element> elements() {
        return elements;
    }

    public List<Wire> wires() {
        return wires;
    }

    public Set<Node> nodes() {
        return nodes;
    }

    private void addOne(CircuitObject object) throws NodesAreAlreadyConnected {
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
                throw new NodesAreAlreadyConnected("Wire ends already connected.");
            }
            if (!nodes.contains(wire.from()) || !nodes.contains(wire.to())) {
                throw new InvalidCircuitObjectAddition("One of the ends is not under model " +
                        "control");
            }
            wires.add(wire);
            wire.from().addWire(wire);
            wire.to().addWire(wire);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void add(CircuitObject object) throws NodesAreAlreadyConnected {
        addOne(object);
        clearNodes();
    }

    public void addAll(Collection<CircuitObject> objects) throws NodesAreAlreadyConnected {
        for (CircuitObject object : objects) {
            addOne(object);
        }
        clearNodes();
    }

    private void removeOne(CircuitObject object) {
        if (object instanceof Node) {
            Node node = (Node) object;
            if (!nodes().contains(node)) {
                throw new InvalidCircuitObjectDeletion("No such element");
            }
            if (!verificator.unique(node)) {
                throw new InvalidCircuitObjectDeletion("Deleted node is the end of the wire.");
            }
            nodes.remove(object);
        } else if (object instanceof Element) {
            Element element = (Element) object;
            if (!elements().contains(element)) {
                throw new InvalidCircuitObjectDeletion("No such element");
            }
            elements.remove(object);
            nodes.remove(element.from());
            nodes.remove(element.to());
        } else if (object instanceof Wire) {
            Wire wire = (Wire) object;
            if (!wires().contains(wire)) {
                throw new InvalidCircuitObjectDeletion("No such element");
            }
            wire.to().deleteWire(wire);
            wire.from().deleteWire(wire);
            if (verificator.unique(wire.from())) {
                remove(wire.from());
            }
            if (verificator.unique(wire.to())) {
                remove(wire.to());
            }
            wires.remove(object);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void remove(CircuitObject object) {
        removeOne(object);
        clearNodes();
    }

    public void removeAll(Collection<CircuitObject> objects) {
        for (CircuitObject object : objects) {
            removeOne(object);
        }
        clearNodes();
    }

    public void removeThenAdd(List<CircuitObject> toBeDeleted, List<CircuitObject> toBeAdded) throws NodesAreAlreadyConnected {
        for (CircuitObject object : toBeDeleted) {
            removeOne(object);
        }
        for (CircuitObject object : toBeAdded) {
            addOne(object);
        }
        clearNodes();
    }

    private void clearNodes() {
        Node node = verificator.findUnnecessaryNode();
        while (node != null) {
            controller.deleteUnnecessaryNodes(node);
            node = verificator.findUnnecessaryNode();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DrawableModel:\n");
        sb.append("Nodes:\n");
        for (Node node : nodes) {
            sb.append(node).append("\n");
        }
        sb.append("Elements:\n");
        for (Element e : elements) {
            sb.append(e).append("\n");
        }
        sb.append("Wires:\n");
        for (Wire wire : wires) {
            sb.append(wire).append("\n");
        }
        return sb.toString();
    }

    public void clear() {
        nodes.clear();
        elements.clear();
        wires.clear();
    }
}
