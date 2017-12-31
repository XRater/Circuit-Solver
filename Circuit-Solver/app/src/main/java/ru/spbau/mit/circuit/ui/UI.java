package ru.spbau.mit.circuit.ui;

import java.util.List;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.NotImplementedYetException;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;
import ru.spbau.mit.circuit.model.circuitObjects.wires.Wire;
import ru.spbau.mit.circuit.model.exceptions.NodesAreAlreadyConnected;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.storage.Converter;

public class UI {
    private final Controller controller;
    private boolean circuitWasLoaded = false;
    private DrawableModel drawableModel;

    public UI(Controller controller) {
        this.controller = controller;
    }

    public void load(Converter.Mode mode, String name) {
        controller.load(mode, name);
    }

    void calculateCurrents() throws CircuitShortingException, NotImplementedYetException {
        controller.calculateCurrents();
    }

    void addToModel(CircuitObject e) throws NodesAreAlreadyConnected {
        controller.add(e);
    }

    void setDrawableModel(DrawableModel drawableModel) {
        this.drawableModel = drawableModel;
    }

    private void clearModel() {
        if (drawableModel != null) {
            drawableModel.clear();
        }
    }

    void removeFromModel(CircuitObject chosen) {
        controller.remove(chosen);
    }

    void removeFromModel(List<CircuitObject> chosen) {
        controller.removeAll(chosen);
    }

    void removeThenAdd(List<CircuitObject> toBeDeleted, List<CircuitObject> toBeAdded)
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
        clearModel();
        circuitWasLoaded = true;
    }

    public void setCircuitWasNotLoaded() {
        clearModel();
        circuitWasLoaded = false;
    }

    public Model getModel() {
        return controller.getModel();
    }

    public void removeFromStorage(Converter.Mode mode, String name) {
        controller.removeFromStorage(mode, name);
    }

    public boolean circuitWasLoaded() {
        return circuitWasLoaded;
    }
}
