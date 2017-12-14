package ru.spbau.mit.circuit.Storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "mytable", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table mytable ("
                + "name text primary key,"
                + "model BLOB" + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table mytable ");
        db.execSQL("create table mytable ("
                + "name text primary key,"
                + "model BLOB" + ");");
    }
}
