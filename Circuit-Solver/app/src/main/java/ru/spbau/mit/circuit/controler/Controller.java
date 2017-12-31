package ru.spbau.mit.circuit.controler;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    // FIXME handle exceptions

    @NonNull
    private final Logic logic;
    @NonNull
    private final UI ui;
    @NonNull
    private final Converter converter;
    private Model model;
    private Activity activity;

    public Controller(@NonNull Activity activity) {
        logic = new Logic(this);
        ui = new UI(this);
        model = new Model(this);
        converter = new Converter(activity);
        this.activity = activity;
    }

    @NonNull
    public Logic getLogic() {
        return logic;
    }

    @NonNull
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
    public void addAll(@NonNull List<CircuitObject> objects) throws NodesAreAlreadyConnected {
        model.addAll(objects);
    }

    public void remove(CircuitObject object) {
        model.remove(object);
    }

    public void removeAll(@NonNull List<CircuitObject> objects) {
        model.removeAll(objects);
    }

    public void removeThenAdd(@NonNull List<CircuitObject> toBeDeleted, @NonNull List<CircuitObject> toBeAdded)
            throws NodesAreAlreadyConnected {
        model.removeThenAdd(toBeDeleted, toBeAdded);
    }

    public void clearModel() {
        model.clear();
    }

    public void deleteUnnecessaryNode(Node common, Wire first, Wire second) {
        ui.deleteUnnecessaryNode(common, first, second);
    }

    public boolean save(Converter.Mode mode, String filename) {
        try {
            return Tasks.saveTask(mode, converter, model).execute(filename).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Nullable
    public List<String> getCircuits(Converter.Mode mode) {
        try {
            return Tasks.getCircuitsTask(mode, converter).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void load(Converter.Mode mode, String filename) {
        try {
            Model newModel = Tasks.loadTask(mode, converter).execute(filename).get();
            newModel.setController(this);
            newModel.initializeVerificator();
            this.model = newModel;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        ui.setCircuitWasLoaded();
        Intent intent = new Intent(activity.getApplicationContext(), NewCircuitActivity.class);
        activity.startActivity(intent);
    }

    public void removeFromStorage(Converter.Mode mode, String name) {
        try {
            Tasks.deleteTask(mode, converter).execute(name).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
