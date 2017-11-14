package ru.spbau.mit.circuit.controler;


import ru.spbau.mit.circuit.logic.Logic;
import ru.spbau.mit.circuit.model.Element;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.ui.UI;

public class Controller {

    private final Logic logic;
    private final UI ui;
    private Model model;

    public Controller() {
        logic = new Logic(this);
        ui = new UI(this);
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
