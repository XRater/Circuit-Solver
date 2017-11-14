package ru.spbau.mit.circuit.logic;

import java.util.List;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.logic.graph.ConnectedGraph;
import ru.spbau.mit.circuit.logic.graph.Graph;
import ru.spbau.mit.circuit.model.Model;

public class Logic {

    private final Controller controller;

    public Logic(Controller controller) {
        this.controller = controller;
    }

    public void calculateCurrents(Model model) {
        Graph g = new Graph(model);
        List<ConnectedGraph> components = g.decompose();
        for (ConnectedGraph component : components) {
            System.out.println(component);
            System.out.println();
            component.solve();
            component.setCurrents();
        }
    }
}
