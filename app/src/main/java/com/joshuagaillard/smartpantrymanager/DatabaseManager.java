package com.joshuagaillard.smartpantrymanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {

    private final DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public void open() {
        database = databaseHelper.getWritableDatabase();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void close() {
        databaseHelper.close();
        database = null;
    }
}