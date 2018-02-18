package ru.spbau.mit.circuit.logic;

import android.support.annotation.NonNull;

import java.util.List;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.logic.graph.ConnectedGraph;
import ru.spbau.mit.circuit.logic.graph.Graph;
import ru.spbau.mit.circuit.model.Model;

public class Logic {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    // required for working with controller if necessary
    private final Controller controller;

    public Logic(Controller controller) {
        this.controller = controller;
    }

    /**
     * The method sets currents and charges for all elements and wires if the circuit.
     *
     * @param model model to calculates currents for
     * @throws CircuitShortingException if there was any kind of shorting
     */

    public void calculateCurrents(@NonNull Model model) throws CircuitShortingException,
            NotImplementedYetException {
        Graph g = new Graph(model);
        List<ConnectedGraph> components = g.decompose();
        for (ConnectedGraph component : components) {
            try {
                component.solve();
            } catch (CircuitShortingException e) {
                throw e;
            } catch (Exception e) {
                throw new NotImplementedYetException(e);
            }
        }
    }
}
