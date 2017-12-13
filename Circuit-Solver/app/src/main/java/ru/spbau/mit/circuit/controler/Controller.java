package ru.spbau.mit.circuit.controler;

import java.util.Collection;
import java.util.List;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.Logic;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.exceptions.NodesAreAlreadyConnected;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.model.node.Node;
import ru.spbau.mit.circuit.ui.UI;

public class Controller {

    private final Logic logic;
    private final UI ui;
    private Model model;

    public Controller() {
        logic = new Logic(this);
        ui = new UI(this);
        model = new Model(this);
    }

    public Logic getLogic() {
        return logic;
    }

    public UI getUi() {
        return ui;
    }

    public Model getModel() {
        return model;
    }

    public void updateView() {
        ui.load(model);
    }

    public void calculateCurrents() throws CircuitShortingException {
        logic.calculateCurrents(model);
    }

    public void add(CircuitObject object) throws NodesAreAlreadyConnected {
        model.add(object);
    }

    public void addAll(Collection<CircuitObject> objects) throws NodesAreAlreadyConnected {
        model.addAll(objects);
    }

    public void remove(CircuitObject object) {
        model.remove(object);
    }

    public void removeAll(Collection<CircuitObject> objects) {
        model.removeAll(objects);
    }

    public void clearModel() {
        model.clear();
    }

    public void deleteUnnecessaryNodes(Node unnecessaryNode) {
        ui.deleteUnnecessaryNodes(unnecessaryNode);
    }


    public void removeThenAdd(List<CircuitObject> toBeDeleted, List<CircuitObject> toBeAdded) throws NodesAreAlreadyConnected {
        model.removeThenAdd(toBeDeleted, toBeAdded);
    }
}
