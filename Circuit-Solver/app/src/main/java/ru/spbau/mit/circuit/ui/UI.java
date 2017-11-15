package ru.spbau.mit.circuit.ui;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.model.Elements.Element;
import ru.spbau.mit.circuit.model.Model;

public class UI {
    private final Controller controller;

    public UI(Controller controller) {
        this.controller = controller;
    }

    public void load(Model model) {
        //TODO
    }

    public void calculateCurrents() {
        controller.calculateCurrents();
    }

    public void addToModel(Element e) {
        controller.addElement(e);
    }
}
