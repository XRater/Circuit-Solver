package ru.spbau.mit.circuit.logic;

import java.util.List;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.logic.graph.ConnectedGraph;
import ru.spbau.mit.circuit.logic.graph.Graph;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.Point;
import ru.spbau.mit.circuit.model.Wire;

public class Logic {

    private final Controller controller;

    public Logic(Controller controller) {
        this.controller = controller;
    }

    public static void main(String[] args) {
        Model model = new Model();
        model.addElement(new Wire(new Point(1, 0), new Point(1, 1)));
//        model.addElement(new Wire(new Point(1, 1), new Point(0, 1)));
        model.addElement(new Wire(new Point(0, 1), new Point(0, 0)));
//        model.addElement(new Wire(new Point(0, 0), new Point(1, 0)));
//        model.addElement(new Wire(new Point(0, 0), new Point(1, 0)));
//        model.addElement(new Wire(new Point(0, 0), new Point(1, 0)));
        Logic logic = new Logic(new Controller());
        logic.calculateCurrents(model);
        //        g.print();
    }

    public void calculateCurrents(Model model) {
        Graph g = new Graph(model);
        List<ConnectedGraph> components = g.decompose();
        System.out.println(components.size());
    }
}
