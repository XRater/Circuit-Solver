package ru.spbau.mit.circuit.controler;

import ru.spbau.mit.circuit.logic.Logic;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.elements.Element;
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

    public void calculateCurrents() {
        logic.calculateCurrents(model);
    }

    public void addElement(Element element) {
        model.addElement(element);
    }

    public boolean removeElement(Element element) {
        return model.removeElement(element);
    }

}
