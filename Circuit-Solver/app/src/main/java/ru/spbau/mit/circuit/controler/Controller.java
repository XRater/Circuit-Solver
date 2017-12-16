package ru.spbau.mit.circuit.controler;

import android.app.Activity;
import android.content.Intent;

import java.io.IOException;
import java.util.List;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.Logic;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.exceptions.NodesAreAlreadyConnected;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.model.node.Node;
import ru.spbau.mit.circuit.storage.Converter;
import ru.spbau.mit.circuit.ui.NewCircuitActivity;
import ru.spbau.mit.circuit.ui.UI;

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
//        localStorage = new Local(activity);
//        driveStorage = new DriveStorage();
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

    //public void updateView() {
    //    ui.load(model);
    //}

    public void calculateCurrents() throws CircuitShortingException {
        logic.calculateCurrents(model);
    }

    public void add(CircuitObject object) throws NodesAreAlreadyConnected {
        model.add(object);
    }

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

    public void deleteUnnecessaryNodes(Node unnecessaryNode) {
        ui.deleteUnnecessaryNodes(unnecessaryNode);
    }

    private Boolean saved = null;

    public boolean save(Converter.Mode mode, String filename) {
        saved = null;
        Thread thread = new Thread(() -> {
            try {
                saved = converter.save(mode, model, filename);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        });
        thread.start();
        while (saved == null) {

        }
        return saved;
    }


    public void load(Converter.Mode mode, String filename) {
        this.model = null;
        Thread thread = new Thread(() -> {
            Model model2 = converter.load(mode, filename);
            model2.setController(this);
            model2.initializeVerificator();
            this.model = model2;
        });
        thread.start();
//        Model model2 = converter.load(mode, filename);
//        model2.setController(this);
//        model2.initializeVerificator();
//        this.model = model2;
//        ui.setCircuitWasLoaded();
        while (model == null) {

        }
        ui.setCircuitWasLoaded();
        Intent intent = new Intent(activity.getApplicationContext(), NewCircuitActivity.class);
        activity.startActivity(intent);
    }


    private List<String> circs;

    public List<String> getCircuits(Converter.Mode mode) {
        circs = null;
        Thread thread = new Thread(() -> {
            circs = converter.getCircuits(mode);
        });
        thread.start();
        while (circs == null) {

        }
        return circs;
    }
}
