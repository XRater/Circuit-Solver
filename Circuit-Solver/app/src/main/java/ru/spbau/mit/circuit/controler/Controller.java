package ru.spbau.mit.circuit.controler;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.List;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.Logic;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.exceptions.NodesAreAlreadyConnected;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.model.node.Node;
import ru.spbau.mit.circuit.ui.UI;

public class Controller {

    private final Logic logic;
    private final UI ui;
    public DBHelper dbHelper;
    private Model model;

    public Controller(Activity activity) {
        logic = new Logic(this);
        ui = new UI(this);
        model = new Model(this);
        dbHelper = new DBHelper(activity.getApplicationContext());
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

    public void addAll(Collection<CircuitObject> objects) throws NodesAreAlreadyConnected {
        model.addAll(objects);
    }

    public void remove(CircuitObject object) {
        model.remove(object);
    }

    public void removeAll(Collection<CircuitObject> objects) {
        model.removeAll(objects);
    }

    public void clearModel() {
        model.clear();
    }

    public void deleteUnnecessaryNodes(Node unnecessaryNode) {
        ui.deleteUnnecessaryNodes(unnecessaryNode);
    }


    public void removeThenAdd(List<CircuitObject> toBeDeleted, List<CircuitObject> toBeAdded) throws NodesAreAlreadyConnected {
        model.removeThenAdd(toBeDeleted, toBeAdded);
    }


    public void saveToLocalDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
//        System.out.println(gson.toJson(model));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(model);
            ByteArrayInputStream in = new ByteArrayInputStream("".getBytes());
            ObjectInputStream objectInputStream = new ObjectInputStream(in);
            Model model2 = (Model) objectInputStream.readObject();
            System.out.println(out.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        cv.put("name", "name");
        cv.put("model", "model");
        db.insert("mytable", null, cv);
        dbHelper.close();
    }

    public void loadFromLocalDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int nameColIndex = c.getColumnIndex("name");
            int modelColIndex = c.getColumnIndex("model");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                System.out.println(
                        "name = " + c.getString(nameColIndex) +
                                ", email = " + c.getString(modelColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        }
        dbHelper.close();
    }
}
