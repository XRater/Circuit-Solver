package ru.spbau.mit.circuit.controler;


import java.util.ArrayList;

import ru.spbau.mit.circuit.logic.Logic;
import ru.spbau.mit.circuit.model.Element;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.ui.UI;

public class Controller {

    private final ArrayList<View> views;
    private Model model;

    public Controller() {
        views = new ArrayList<>();
        views.add(new Logic(this));
        views.add(new UI(this));
    }

    public void updateView() {
        for (View view : views)
            view.update(model);
    }

    public void addElement(Element element) {
        model.addElement(element);
    }

    public boolean removeElement(Element element) {
        return model.removeElement(element);
    }
}
