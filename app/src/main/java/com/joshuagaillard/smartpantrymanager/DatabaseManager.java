package com.joshuagaillard.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public long insertPantryItem(
            String ingredientName,
            double quantity,
            String unit,
            String expiryDate) {

        ContentValues values = new ContentValues();

        values.put(
                DatabaseHelper.COLUMN_INGREDIENT_NAME,
                ingredientName
        );

        values.put(
                DatabaseHelper.COLUMN_QUANTITY,
                quantity
        );

        values.put(
                DatabaseHelper.COLUMN_UNIT,
                unit
        );

        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            values.putNull(DatabaseHelper.COLUMN_EXPIRY_DATE);
        } else {
            values.put(
                    DatabaseHelper.COLUMN_EXPIRY_DATE,
                    expiryDate
            );
        }

        return database.insert(
                DatabaseHelper.TABLE_PANTRY_ITEMS,
                null,
                values
        );
    }

    public Cursor getAllPantryItems() {

        return database.query(
                DatabaseHelper.TABLE_PANTRY_ITEMS,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.COLUMN_INGREDIENT_NAME + " ASC"
        );
    }

    public Cursor getPantryItemById(long pantryItemId) {

        return database.query(
                DatabaseHelper.TABLE_PANTRY_ITEMS,
                null,
                DatabaseHelper.COLUMN_PANTRY_ID + " = ?",
                new String[]{
                        String.valueOf(pantryItemId)
                },
                null,
                null,
                null
        );
    }

    public int updatePantryItem(
            long pantryItemId,
            String ingredientName,
            double quantity,
            String unit,
            String expiryDate) {

        ContentValues values = new ContentValues();

        values.put(
                DatabaseHelper.COLUMN_INGREDIENT_NAME,
                ingredientName
        );

        values.put(
                DatabaseHelper.COLUMN_QUANTITY,
                quantity
        );

        values.put(
                DatabaseHelper.COLUMN_UNIT,
                unit
        );

        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            values.putNull(DatabaseHelper.COLUMN_EXPIRY_DATE);
        } else {
            values.put(
                    DatabaseHelper.COLUMN_EXPIRY_DATE,
                    expiryDate
            );
        }

        return database.update(
                DatabaseHelper.TABLE_PANTRY_ITEMS,
                values,
                DatabaseHelper.COLUMN_PANTRY_ID + " = ?",
                new String[]{
                        String.valueOf(pantryItemId)
                }
        );
    }

    public int deletePantryItem(long pantryItemId) {

        return database.delete(
                DatabaseHelper.TABLE_PANTRY_ITEMS,
                DatabaseHelper.COLUMN_PANTRY_ID + " = ?",
                new String[]{
                        String.valueOf(pantryItemId)
                }
        );
    }

    public Cursor getAllRecipes() {

        return database.query(
                DatabaseHelper.TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.COLUMN_RECIPE_NAME + " ASC"
        );
    }

    public Cursor getRecipeIngredients(long recipeId) {

        return database.query(
                DatabaseHelper.TABLE_RECIPE_INGREDIENTS,
                null,
                DatabaseHelper.COLUMN_RECIPE_INGREDIENT_RECIPE_ID + " = ?",
                new String[]{
                        String.valueOf(recipeId)
                },
                null,
                null,
                DatabaseHelper.COLUMN_RECIPE_INGREDIENT_ID + " ASC"
        );
    }

    public void close() {
        databaseHelper.close();
        database = null;
    }
}