package ru.spbau.mit.circuit.model.interfaces;


import java.util.List;

import ru.spbau.mit.circuit.model.elements.Wire;

public interface Wireable {
    List<Wire> wires();
}
