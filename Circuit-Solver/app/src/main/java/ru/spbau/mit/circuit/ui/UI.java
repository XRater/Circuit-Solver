package ru.spbau.mit.circuit.ui;


import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.controler.View;
import ru.spbau.mit.circuit.model.Model;

public class UI implements View {

    private final Controller controller;

    public UI(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void update(Model model) {
        //TODO
    }
}
