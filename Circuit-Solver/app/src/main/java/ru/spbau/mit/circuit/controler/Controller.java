package ru.spbau.mit.circuit.controler;


import java.util.ArrayList;

import ru.spbau.mit.circuit.logic.Logic;
import ru.spbau.mit.circuit.model.Element;
import ru.spbau.mit.circuit.model.Model;

public class Controller {

    private Model model;
    private final ArrayList<View> views;

    public Controller() {
        views = new ArrayList<>();
        views.add(new Logic(this));
        //TODO add UI here
    }

    public void updateView() {
        for (View view : views)
            view.update(model);
    }

    //TODO events from views
}
