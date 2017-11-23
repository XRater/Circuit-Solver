package ru.spbau.mit.circuit.ui;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.model.InvalidCircuitObjectAddition;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;

public class UI {
    private final Controller controller;
    private DrawableModel drawableModel;

    public UI(Controller controller) {
        this.controller = controller;
    }

    public void load(Model model) {
        //TODO
    }

    public void calculateCurrents() throws CircuitShortingException {
        controller.calculateCurrents();
    }

    public void addToModel(CircuitObject e) throws InvalidCircuitObjectAddition {
        controller.add(e);
    }

    public void setDrawableModel(DrawableModel drawableModel) {
        this.drawableModel = drawableModel;
    }

    public void clearModel() {
        if (drawableModel != null) {
            drawableModel.clear();
        }
    }
}
