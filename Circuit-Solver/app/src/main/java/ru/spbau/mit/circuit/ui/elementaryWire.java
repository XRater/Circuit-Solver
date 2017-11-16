package ru.spbau.mit.circuit.ui;

import ru.spbau.mit.circuit.model.elements.Item;

public class elementaryWire {
    public boolean have = false;
    public Item item1;
    public Item item2;

    public void addElementaryWire(Item chosen, Item other) {
        have = true;
        item1 = chosen;
        item2 = other;
    }
}
