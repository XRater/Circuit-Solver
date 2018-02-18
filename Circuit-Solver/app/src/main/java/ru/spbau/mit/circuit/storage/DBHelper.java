package ru.spbau.mit.circuit.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "mytable", null, 1);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL("create table mytable ("
                + "name text primary key,"
                + "model BLOB" + ");");

    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table mytable ");
        db.execSQL("create table mytable ("
                + "name text primary key,"
                + "model BLOB" + ");");
    }
}
