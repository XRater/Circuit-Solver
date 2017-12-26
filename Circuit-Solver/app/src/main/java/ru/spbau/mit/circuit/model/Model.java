package ru.spbau.mit.circuit.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
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

public class Model implements Serializable {
    private transient Controller controller;
    private List<Element> elements = new ArrayList<>();
    private List<Wire> wires = new ArrayList<>();
    private Set<Node> nodes = new HashSet<>();

    private transient Verificator verificator = new Verificator(this);

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

    public void add(CircuitObject object) throws NodesAreAlreadyConnected {
        addAll(Collections.singletonList(object));
    }

    public void addAll(List<CircuitObject> objects) throws NodesAreAlreadyConnected {
        removeThenAdd(Collections.emptyList(), objects);
    }

    public void remove(CircuitObject object) {
        removeAll(Collections.singletonList(object));
    }

    public void removeAll(List<CircuitObject> objects) {
        try {
            removeThenAdd(objects, Collections.emptyList());
        } catch (NodesAreAlreadyConnected nodesAreAlreadyConnected) {
            throw new RuntimeException(); // Should never happen
        }
    }

    public void removeThenAdd(List<CircuitObject> toBeDeleted, List<CircuitObject> toBeAdded)
            throws NodesAreAlreadyConnected {
        List<CircuitObject> deleted = new LinkedList<>();
        List<CircuitObject> added = new LinkedList<>();
        for (CircuitObject object : toBeDeleted) {
            removeOne(object);
            deleted.add(object); // collecting changes
        }

        try {
            for (CircuitObject object : toBeAdded) {
                addOne(object);
                added.add(object); // collecting changes
            }
        } catch (NodesAreAlreadyConnected e) {
            try {
                Collections.reverse(deleted);
                Collections.reverse(added);
                removeThenAdd(added, deleted); // applying changes in the reverse order
            } catch (NodesAreAlreadyConnected unexpected) {
                throw new RuntimeException(); // Should never happen
            }
            throw e;
        }

        clearNodes();
    }

    public void clear() {
        nodes.clear();
        elements.clear();
        wires.clear();
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

    private void removeOne(CircuitObject object) {
        if (object instanceof Node) {
            Node node = (Node) object;
            if (!nodes().contains(node)) {
                throw new InvalidCircuitObjectDeletion("No such element");
            }
            if (!verificator.isolated(node)) {
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
            if (verificator.isolated(wire.from())) {
                remove(wire.from());
            }
            if (verificator.isolated(wire.to())) {
                remove(wire.to());
            }
            wires.remove(object);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void clearNodes() {
        Node node = verificator.findUnnecessaryNode();
        while (node != null) {

            Iterator<Wire> iter = node.wires().iterator();
            Wire first = iter.next();
            Wire second = iter.next();

            if (iter.hasNext()) {
                throw new RuntimeException(); // should never happen
            }

            Node common = Wire.findCommon(first, second);
            Node from = first.opposite(common);
            Node to = second.opposite(common);

            first.replace(from, to);
            removeOne(second);
            removeOne(common);

            controller.deleteUnnecessaryNode(common, first, second);

            node = verificator.findUnnecessaryNode();
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void initializeVerificator() {
        verificator = new Verificator(this);
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
}
