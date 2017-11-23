package ru.spbau.mit.circuit.model.interfaces;


import java.util.Collection;

import ru.spbau.mit.circuit.model.elements.Wire;

public interface Wireable {
    Collection<Wire> wires();
}
