package com.joshuagaillard.smartpantrymanager;

import android.database.Cursor;

import java.util.Locale;

public class RecipeMatcher {

    private final DatabaseManager databaseManager;

    public RecipeMatcher(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean hasEveryRequiredIngredient(long recipeId) {

        Cursor recipeIngredientsCursor =
                databaseManager.getRecipeIngredients(recipeId);

        try {
            while (recipeIngredientsCursor.moveToNext()) {

                String requiredIngredient =
                        recipeIngredientsCursor.getString(
                                recipeIngredientsCursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_RECIPE_INGREDIENT_NAME
                                )
                        );

                double requiredQuantity =
                        recipeIngredientsCursor.getDouble(
                                recipeIngredientsCursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_REQUIRED_QUANTITY
                                )
                        );

                String requiredUnit =
                        recipeIngredientsCursor.getString(
                                recipeIngredientsCursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_REQUIRED_UNIT
                                )
                        );

                if (!pantryContainsEnough(
                        requiredIngredient,
                        requiredQuantity,
                        requiredUnit)) {

                    return false;
                }
            }

            return true;

        } finally {
            recipeIngredientsCursor.close();
        }
    }

    private boolean pantryContainsEnough(
            String requiredIngredient,
            double requiredQuantity,
            String requiredUnit) {

        Cursor pantryCursor = databaseManager.getAllPantryItems();

        try {
            while (pantryCursor.moveToNext()) {

                String pantryIngredient =
                        pantryCursor.getString(
                                pantryCursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_INGREDIENT_NAME
                                )
                        );

                double pantryQuantity =
                        pantryCursor.getDouble(
                                pantryCursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_QUANTITY
                                )
                        );

                String pantryUnit =
                        pantryCursor.getString(
                                pantryCursor.getColumnIndexOrThrow(
                                        DatabaseHelper.COLUMN_UNIT
                                )
                        );

                String normalizedPantryIngredient =
                        normalizeIngredientName(pantryIngredient);

                String normalizedRequiredIngredient =
                        normalizeIngredientName(requiredIngredient);

                String normalizedPantryUnit =
                        normalizeUnit(pantryUnit);

                String normalizedRequiredUnit =
                        normalizeUnit(requiredUnit);

                if (normalizedPantryIngredient.equals(
                        normalizedRequiredIngredient)
                        && normalizedPantryUnit.equals(
                        normalizedRequiredUnit)) {

                    return pantryQuantity >= requiredQuantity;
                }
            }

            return false;

        } finally {
            pantryCursor.close();
        }
    }

    private String normalizeIngredientName(String ingredientName) {

        if (ingredientName == null) {
            return "";
        }

        String normalized =
                ingredientName
                        .trim()
                        .toLowerCase(Locale.ROOT)
                        .replaceAll("\\s+", " ");

        String[] words = normalized.split(" ");

        if (words.length == 0) {
            return normalized;
        }

        int lastIndex = words.length - 1;
        words[lastIndex] = singularize(words[lastIndex]);

        return String.join(" ", words);
    }

    private String normalizeUnit(String unit) {

        if (unit == null) {
            return "";
        }

        String normalized =
                unit.trim().toLowerCase(Locale.ROOT);

        switch (normalized) {

            case "piece":
            case "pieces":
                return "piece";

            case "slice":
            case "slices":
                return "slice";

            case "clove":
            case "cloves":
                return "clove";

            case "leaf":
            case "leaves":
                return "leaf";

            case "gram":
            case "grams":
            case "g":
                return "g";

            case "millilitre":
            case "millilitres":
            case "milliliter":
            case "milliliters":
            case "ml":
                return "ml";

            default:
                return normalized;
        }
    }

    private String singularize(String word) {

        if (word.endsWith("ies") && word.length() > 3) {
            return word.substring(0, word.length() - 3) + "y";
        }

        if (word.endsWith("oes") && word.length() > 3) {
            return word.substring(0, word.length() - 2);
        }

        if (word.endsWith("s")
                && !word.endsWith("ss")
                && word.length() > 1) {

            return word.substring(0, word.length() - 1);
        }

        return word;
    }
}