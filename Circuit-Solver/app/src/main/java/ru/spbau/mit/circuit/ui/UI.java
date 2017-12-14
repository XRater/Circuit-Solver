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

    public void addToModel(List<CircuitObject> e) throws NodesAreAlreadyConnected {
        controller.addAll(e);
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

    public void removeFromModel(List<CircuitObject> chosen) {
        controller.removeAll(chosen);
    }

    public void deleteUnnecessaryNodes(Node unnecessaryNode) {
        drawableModel.deleteUnnecessaryNodes(unnecessaryNode);
    }

    public void removeThenAdd(List<CircuitObject> toBeDeleted, List<CircuitObject> toBeAdded) throws NodesAreAlreadyConnected {
        controller.removeThenAdd(toBeDeleted, toBeAdded);
    }
}
