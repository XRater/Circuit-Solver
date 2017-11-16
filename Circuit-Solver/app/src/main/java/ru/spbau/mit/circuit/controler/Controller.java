package ru.spbau.mit.circuit.controler;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.Logic;
import ru.spbau.mit.circuit.model.CircuitObject;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.ui.UI;

public class Controller {

    private final Logic logic = new Logic(this);
    private final UI ui = new UI(this);
    private Model model = new Model();

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

    public void add(CircuitObject object) {
        model.add(object);
    }

    public boolean remove(CircuitObject object) {
        return model.remove(object);
    }

    public void clearModel() {
        model.clear();
    }
}
