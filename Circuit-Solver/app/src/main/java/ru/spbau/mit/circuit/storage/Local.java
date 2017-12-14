package ru.spbau.mit.circuit.storage;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Local implements Storage {
    private DBHelper dbHelper;

    public Local(Activity activity) {
        dbHelper = new DBHelper(activity.getApplicationContext());
    }

    @Override
    public void save(byte[] bytes, String name) throws SQLException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("model", bytes);

        db.insert("mytable", null, cv);
        dbHelper.close();
    }

    @Override
    public List<String> getCircuits() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);
        ArrayList<String> names = new ArrayList<>();
        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("name");
            do {
                names.add(c.getString(nameColIndex));
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();
        return names;
    }

    @Override
    public ByteArrayInputStream load(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("name");
            int modelColIndex = c.getColumnIndex("model");

            do {
                if (c.getString(nameColIndex).equals(name)) {
                    return new ByteArrayInputStream(c.getBlob(modelColIndex));
                }
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();
        return null;
    }

}
