package ru.spbau.mit.circuit.ui;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.model.CircuitObject;
import ru.spbau.mit.circuit.model.Model;

public class UI {
    private final Controller controller;

    public UI(Controller controller) {
        this.controller = controller;
    }

    public void load(Model model) {
        //TODO
    }

    public void calculateCurrents() throws CircuitShortingException {
        controller.calculateCurrents();
    }

    public void addToModel(CircuitObject e) {
        controller.add(e);
    }
}
