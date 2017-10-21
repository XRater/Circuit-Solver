package ru.spbau.mit.circuit.logic;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.controler.View;
import ru.spbau.mit.circuit.model.Model;

public class Logic implements View {

    private final Controller controller;

    public Logic(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void update(Model model) {
    }
}
