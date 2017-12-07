package ru.spbau.mit.circuit.ui;

import java.util.List;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.exceptions.NodesAreAlreadyConnected;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.model.node.Node;

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

    public void addToModel(CircuitObject e) throws NodesAreAlreadyConnected {
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

    public void removeFromModel(CircuitObject chosen) {
        controller.remove(chosen);
    }

    public void deleteUnnecessaryNodes(List<Node> unnecessaryNodes) {
        drawableModel.deleteUnnecessaryNodes(unnecessaryNodes);
    }
}
