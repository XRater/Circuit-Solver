package ru.spbau.mit.circuit.controler;

import android.app.Activity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.Logic;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.exceptions.NodesAreAlreadyConnected;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.model.node.Node;
import ru.spbau.mit.circuit.storage.Converter;
import ru.spbau.mit.circuit.storage.DBHelper;
import ru.spbau.mit.circuit.storage.Drive;
import ru.spbau.mit.circuit.storage.Local;
import ru.spbau.mit.circuit.ui.UI;

public class Controller {

    private final Logic logic;
    private final UI ui;
    private final Converter converter;
    public DBHelper dbHelper;
    private Model model;
    private Local localStorage;
    private Drive driveStorage;

    public Controller(Activity activity) {
        logic = new Logic(this);
        ui = new UI(this);
        model = new Model(this);
        converter = new Converter(activity);
//        localStorage = new Local(activity);
//        driveStorage = new Drive();
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

    public void updateView() {
        ui.load(model);
    }

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

    public void saveToNewFile(int storageNumber, Model model, String filename) {
        try {
            converter.saveToNewFile(storageNumber, model, filename);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public Collection<String> getFiles(int storageNumber) {
        return converter.getFiles(storageNumber);
    }

    public Model loadFromFile(int storageNumber, String filename) {
        try {
            return converter.loadFromFile(storageNumber, filename);
        } catch (IOException e) {
            throw new RuntimeException();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }
    }

    public void saveToLocalDB() {
        try {
            localStorage.save(model);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadFromLocalDB() {
        localStorage.load();
    }
}
