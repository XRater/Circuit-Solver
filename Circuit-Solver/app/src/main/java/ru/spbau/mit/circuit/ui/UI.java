package ru.spbau.mit.circuit.ui;

import java.util.List;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.elements.Wire;
import ru.spbau.mit.circuit.model.exceptions.NodesAreAlreadyConnected;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.model.node.Node;
import ru.spbau.mit.circuit.storage.Converter;

public class UI {
    private final Controller controller;
    public boolean circuitWasLoaded = false;
    private DrawableModel drawableModel;

    public UI(Controller controller) {
        this.controller = controller;
    }

    public void load(Converter.Mode mode, String name) {
        controller.load(mode, name);
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

    public void removeThenAdd(List<CircuitObject> toBeDeleted, List<CircuitObject> toBeAdded)
            throws NodesAreAlreadyConnected {
        controller.removeThenAdd(toBeDeleted, toBeAdded);
    }

    public void deleteUnnecessaryNode(Node common, Wire first, Wire second) {
        drawableModel.deleteUnnecessaryNode(common, first, second);
    }

    public boolean save(Converter.Mode mode, String name) {
        return controller.save(mode, name);
    }

    public List<String> getCircuits(Converter.Mode mode) {
        return controller.getCircuits(mode);
    }

    public void setCircuitWasLoaded() {
        circuitWasLoaded = true;
    }

    public void setCircuitWasNotLoaded() {
        circuitWasLoaded = false;
    }

    public Model getModel() {
        return controller.getModel();
    }
}
