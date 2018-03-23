package ru.spbau.mit.circuit.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
import ru.spbau.mit.circuit.storage.StorageException;

/**
 * Base class that calls methods of Controller.
 */
public class UI {
    private final Controller controller;
    private boolean circuitWasLoaded = false;
    private DrawableModel drawableModel;

    public UI(Controller controller) {
        this.controller = controller;
    }

    public void load(Converter.Mode mode, String name) throws StorageException {
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

    void removeFromModel(@NonNull List<CircuitObject> chosen) {
        controller.removeAll(chosen);
    }

    void removeThenAdd(@NonNull List<CircuitObject> toBeDeleted, @NonNull List<CircuitObject>
            toBeAdded)
            throws NodesAreAlreadyConnected {
        controller.removeThenAdd(toBeDeleted, toBeAdded);
    }

    public void deleteUnnecessaryNode(@NonNull Node common, Wire first, Wire second) {
        drawableModel.deleteUnnecessaryNode(common, first, second);
    }

    public void deleteUnnecessaryNode(@NonNull Node node, Wire wire) {
        drawableModel.deleteUnnecessaryNode(node, wire);
    }

    public void deleteUnnecessaryNode(@NonNull Node common) {
        drawableModel.deleteUnnecessaryNode(common);
    }

    public boolean save(Converter.Mode mode, String name) throws StorageException {
        return controller.save(mode, name);
    }

    @Nullable
    public List<String> getCircuits(Converter.Mode mode) throws StorageException {
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

    public void removeFromStorage(Converter.Mode mode, String name) throws StorageException {
        controller.removeFromStorage(mode, name);
    }

    public boolean circuitWasLoaded() {
        return circuitWasLoaded;
    }
}
