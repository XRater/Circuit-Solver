package ru.spbau.mit.circuit.storage;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;

import ru.spbau.mit.circuit.model.Model;

public class Local implements Storage {
    private DBHelper dbHelper;

    public Local(Activity activity) {
        dbHelper = new DBHelper(activity.getApplicationContext());
    }

    public void save(Model model) throws IOException, SQLException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        byte[] bytes = Converter.serialize(model);
        System.out.println(bytes.toString());
//        java.sql.Blob blob = null;
//        blob.setBytes(1, bytes);

        cv.put("name", "Model");
        cv.put("model", bytes);
        db.insert("mytable", null, cv);
        dbHelper.close();
    }

    public Model load() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int nameColIndex = c.getColumnIndex("name");
            int modelColIndex = c.getColumnIndex("model");

            do {

//                System.out.println(
//                        "name = " + c.getString(nameColIndex) +
//                                ", Model = " + c.getBlob(modelColIndex));
                if (c.getString(nameColIndex).equals("Model")) {
                    try {
                        return Converter.deserialize(new ByteArrayInputStream(c.getBlob
                                (modelColIndex)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();
        return null;
    }

}
