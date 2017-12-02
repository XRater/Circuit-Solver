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


    public void calculateCurrents(Model model) throws CircuitShortingException {
        Graph g = new Graph(model);
        List<ConnectedGraph> components = g.decompose();
        for (ConnectedGraph component : components) {
            System.out.println(component);
//                    component.solve();
            component.setCurrents();
        }
    }

    public static void main(String[] args) {

    }
}
