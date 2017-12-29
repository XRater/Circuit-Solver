package ru.spbau.mit.circuit.model.interfaces;


import java.util.Collection;

import ru.spbau.mit.circuit.model.circuitObjects.wires.Wire;

/**
 * Common interface for nodes and elements.
 */
public interface Wireable {


    /**
     * The method returns all wires adjacent to the element.
     */
    Collection<Wire> wires();
}
