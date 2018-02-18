package ru.spbau.mit.circuit.controler;

import android.app.Activity;
import android.content.Intent;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.Logic;
import ru.spbau.mit.circuit.logic.NotImplementedYetException;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;
import ru.spbau.mit.circuit.model.circuitObjects.wires.Wire;
import ru.spbau.mit.circuit.model.exceptions.NodesAreAlreadyConnected;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.storage.Converter;
import ru.spbau.mit.circuit.ui.NewCircuitActivity;
import ru.spbau.mit.circuit.ui.UI;

/**
 * Class connector between all other parts of the project (Logic, UI, Converter). Used only
 * to pass messages. There might be no useful functionality.
 */
public class Controller {

    private final Logic logic;
    private final UI ui;
    private final Converter converter;
    private Model model;
    private Activity activity;

    public Controller(Activity activity) {
        logic = new Logic(this);
        ui = new UI(this);
        model = new Model(this);
        converter = new Converter(activity);
        this.activity = activity;
    }

    public Logic getLogic() {
        return logic;
    }

    public UI getUi() {
        return ui;
    }

    public Model getModel() {
        return model;
    }

    public void calculateCurrents() throws CircuitShortingException, NotImplementedYetException {
        logic.calculateCurrents(model);
    }

    public void add(CircuitObject object) throws NodesAreAlreadyConnected {
        model.add(object);
    }

    @SuppressWarnings("unused")
    public void addAll(List<CircuitObject> objects) throws NodesAreAlreadyConnected {
        model.addAll(objects);
    }

    public void remove(CircuitObject object) {
        model.remove(object);
    }

    public void removeAll(List<CircuitObject> objects) {
        model.removeAll(objects);
    }

    public void removeThenAdd(List<CircuitObject> toBeDeleted, List<CircuitObject> toBeAdded)
            throws NodesAreAlreadyConnected {
        model.removeThenAdd(toBeDeleted, toBeAdded);
    }

    public void clearModel() {
        model.clear();
    }

    public void deleteUnnecessaryNode(Node common, Wire first, Wire second) {
        ui.deleteUnnecessaryNode(common, first, second);
    }

    public Boolean save(Converter.Mode mode, String filename) {
        try {
            return Tasks.saveTask(mode, converter, model).execute(filename).get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    public List<String> getCircuits(Converter.Mode mode) {
        try {
            return Tasks.getCircuitsTask(mode, converter).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    public Boolean load(Converter.Mode mode, String filename) {
        try {
            Model newModel = Tasks.loadTask(mode, converter).execute(filename).get();
            newModel.setController(this);
            newModel.initializeVerificator();
            this.model = newModel;
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }

        ui.setCircuitWasLoaded();
        Intent intent = new Intent(activity.getApplicationContext(), NewCircuitActivity.class);
        activity.startActivity(intent);
        return true;
    }

    public Boolean removeFromStorage(Converter.Mode mode, String name) {
        try {
            Tasks.deleteTask(mode, converter).execute(name).get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
        return true;
    }
}
