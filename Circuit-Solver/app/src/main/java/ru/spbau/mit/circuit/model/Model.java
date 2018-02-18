package ru.spbau.mit.circuit.model;


import android.support.annotation.NonNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.model.circuitObjects.elements.Element;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;
import ru.spbau.mit.circuit.model.circuitObjects.wires.Wire;
import ru.spbau.mit.circuit.model.exceptions.InvalidCircuitObjectAddition;
import ru.spbau.mit.circuit.model.exceptions.InvalidCircuitObjectDeletion;
import ru.spbau.mit.circuit.model.exceptions.NodesAreAlreadyConnected;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;

/**
 * Model class. Represents structure of the circuit. Any model represented by this class will be
 * legal after calling any public method.
 * <p>
 * Model has strong-exception guarantees. Therefore if any exception occurred while changing the
 * model all changes will be canceled.
 * <p>
 * Also there is no possible way for model to create any circuit object inside of it. All objects
 * will be added from outside, therefore you may use your invariants of stored in model objects.
 */
public class Model implements Serializable {

    private transient Controller controller; // controller
    @NonNull
    private transient Verificator verificator = new Verificator(this); // verificator

    @NonNull
    private List<Element> elements = new ArrayList<>(); // elements
    @NonNull
    private List<Wire> wires = new ArrayList<>(); // wires
    @NonNull
    private Set<Node> nodes = new HashSet<>(); // nodes

    public Model(Controller controller) {
        this.controller = controller;
    }

    //some getters
    public List<Element> elements() {
        return ImmutableList.copyOf(elements);
    }

    public List<Wire> wires() {
        return ImmutableList.copyOf(wires);
    }

    public Set<Node> nodes() {
        return ImmutableSet.copyOf(nodes);
    }

    // this methods are required in case we loaded new circuit instead of creating new one.
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void initializeVerificator() {
        verificator = new Verificator(this);
    }

    /**
     * The method adds one object from the model. Must be legal.
     *
     * @param object object to add
     * @throws NodesAreAlreadyConnected if addition was not needed cause nodes were already
     *                                  connected.
     */
    public void add(CircuitObject object) throws NodesAreAlreadyConnected {
        addAll(Collections.singletonList(object));
    }

    /**
     * The method consequently adds all objects from the list to model. Result action must be legal.
     **/
    public void addAll(@NonNull List<CircuitObject> objects) throws NodesAreAlreadyConnected {
        removeThenAdd(Collections.emptyList(), objects);
    }

    /**
     * The method remove one object from the model. Must be legal.
     *
     * @param object object to remove
     */
    public void remove(CircuitObject object) {
        removeAll(Collections.singletonList(object));
    }

    /**
     * The method consequently removes all objects from the list to model. Result action must be
     * legal.
     **/
    public void removeAll(@NonNull List<CircuitObject> objects) {
        try {
            removeThenAdd(objects, Collections.emptyList());
        } catch (NodesAreAlreadyConnected nodesAreAlreadyConnected) {
            throw new RuntimeException(); // Should never happen
        }
    }

    /**
     * This method consequently removes all elements from the deletions list
     * and continues with adding all elements from additions list.
     * <p>
     * Result action must be legal.
     *
     * @param toBeDeleted list to remove at first
     * @param toBeAdded   list to add after
     */
    public void removeThenAdd(@NonNull List<CircuitObject> toBeDeleted, @NonNull List<CircuitObject> toBeAdded)
            throws NodesAreAlreadyConnected {
        List<CircuitObject> deleted = new LinkedList<>();
        List<CircuitObject> added = new LinkedList<>();
        for (CircuitObject object : toBeDeleted) {
            removeOne(object);
            deleted.add(object); // Collecting changes
        }

        try {
            for (CircuitObject object : toBeAdded) {
                addOne(object);
                added.add(object); // Collecting changes
            }
        } catch (NodesAreAlreadyConnected e) {
            try {
                Collections.reverse(deleted);
                Collections.reverse(added);
                removeThenAdd(added, deleted); // Applying changes in the reverse order
            } catch (NodesAreAlreadyConnected unexpected) {
                throw new RuntimeException(); // Should never happen
            }
            throw e;
        }

        clearNodes(); // make model legal
    }

    /**
     * Clears the model.
     */
    public void clear() {
        nodes.clear();
        elements.clear();
        wires.clear();
    }

    /**
     * Inner add method. Model might be incorrect after this method.
     */
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

    /**
     * Inner add method. Model might be incorrect after this method.
     */
    private void removeOne(CircuitObject object) {
        if (object instanceof Node) {
            Node node = (Node) object;
            if (!nodes().contains(node)) {
                throw new InvalidCircuitObjectDeletion("No such element");
            }
            if (!verificator.isIsolated(node)) {
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
            if (verificator.isIsolated(wire.from())) {
                remove(wire.from());
            }
            if (verificator.isIsolated(wire.to())) {
                remove(wire.to());
            }
            wires.remove(object);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * This method removes node with small edge-degree from the model control and connects
     * adjacent edges into one edge.
     */
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

    @NonNull
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
