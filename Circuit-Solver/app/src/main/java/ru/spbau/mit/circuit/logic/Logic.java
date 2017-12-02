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

//    public static void main(String[] args) {
//        Model model = new Model();
//        model.addElement(new Wire(new Point(1, 0), new Point(1, 1)));
//        model.addElement(new Wire(new Point(1, 1), new Point(0, 1)));
//        model.addElement(new Battery(new Point(0, 0), new Point(0, 1)));
//        model.addElement(new Wire(new Point(0, 0), new Point(1, 0)));
//        model.addElement(new Wire(new Point(1, 0), new Point(2, 0)));
//        model.addElement(new Wire(new Point(1, 1), new Point(2, 1)));
//        model.addElement(new Wire(new Point(2, 0), new Point(2, 1)));
//        Logic logic = new Logic(new Controller());
//        logic.calculateCurrents(model);
//    }

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
