package ru.spbau.mit.circuit.model;


import java.util.ArrayList;

public class Model {
    private ArrayList<Element> elements = new ArrayList<>();

    public ArrayList<Element> getElements() {
        return elements;
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public boolean removeElement(Element element) {
        return elements.remove(element);
    }
}
