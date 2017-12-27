package ru.spbau.mit.circuit.controler;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.Logic;
import ru.spbau.mit.circuit.logic.ToHardException;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.elements.Wire;
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

    public void calculateCurrents() throws CircuitShortingException, ToHardException {
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


    public void deleteUnnecessaryNode(Node common, Wire first, Wire second) {
        ui.deleteUnnecessaryNode(common, first, second);
    }


    public boolean save(Converter.Mode mode, String filename) {
        try {
            return (Boolean) new SaveTask(mode, converter, model).execute(filename).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getCircuits(Converter.Mode mode) {
        try {
            return (List<String>) new GetCircuitsTask(mode, converter).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void load(Converter.Mode mode, String filename) {
        try {
            Model newModel = (Model) new LoadTask(mode, converter).execute(filename).get();
            newModel.setController(this);
            newModel.initializeVerificator();
            this.model = newModel;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ui.setCircuitWasLoaded();
        Intent intent = new Intent(activity.getApplicationContext(), NewCircuitActivity.class);
        activity.startActivity(intent);
    }

    static abstract class AbstractTask extends AsyncTask<String, Void, Object> {
        protected Converter.Mode mode;
        protected Converter converter;

        AbstractTask(Converter.Mode mode, Converter converter) {
            this.converter = converter;
            this.mode = mode;
        }
    }

    static class LoadTask extends AbstractTask {
        LoadTask(Converter.Mode mode, Converter converter) {
            super(mode, converter);
        }

        @Override
        protected Model doInBackground(String... filename) {
            return converter.load(mode, filename[0]);
        }
    }

    static class SaveTask extends AbstractTask {
        private Model model;

        SaveTask(Converter.Mode mode, Converter converter, Model model) {
            super(mode, converter);
            this.model = model;
        }

        @Override
        protected Object doInBackground(String... filename) {
            try {
                return converter.save(mode, model, filename[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    static class GetCircuitsTask extends AbstractTask {

        GetCircuitsTask(Converter.Mode mode, Converter converter) {
            super(mode, converter);
        }

        @Override
        protected Object doInBackground(String... filename) {
            return converter.getCircuits(mode);
        }
    }
}
