package ru.spbau.mit.circuit.logic;

import java.util.List;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.logic.graph.ConnectedGraph;
import ru.spbau.mit.circuit.logic.graph.Graph;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.elements.Resistor;
import ru.spbau.mit.circuit.model.point.Point;

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
            //            component.solve();
//            component.setCurrents();
        }
    }

    public static void main(String[] args) {
        Model m = new Model();
        m.addElement(new Resistor(new Point(10, 10), new Point(10, 20)));
    }
}
