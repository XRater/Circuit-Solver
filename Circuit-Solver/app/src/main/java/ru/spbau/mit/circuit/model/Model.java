package ru.spbau.mit.circuit.model;


import java.util.ArrayList;

import ru.spbau.mit.circuit.model.elements.Element;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Model:\n");
        for (Element e : elements) {
            sb.append(e).append("\n");
        }
        return sb.toString();
    }

    public void clear() {
        elements.clear();
    }
}
