package com.joshuagaillard.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantryManager.db";
    private static final int DATABASE_VERSION = 1;

    // Pantry items table
    public static final String TABLE_PANTRY_ITEMS = "pantry_items";

    public static final String COLUMN_PANTRY_ID = "pantry_id";
    public static final String COLUMN_INGREDIENT_NAME = "ingredient_name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";

    // Recipes table
    public static final String TABLE_RECIPES = "recipes";

    public static final String COLUMN_RECIPE_ID = "recipe_id";
    public static final String COLUMN_RECIPE_NAME = "recipe_name";
    public static final String COLUMN_PREPARATION_STEPS = "preparation_steps";

    // Recipe ingredients table
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    public static final String COLUMN_RECIPE_INGREDIENT_ID =
            "recipe_ingredient_id";

    public static final String COLUMN_RECIPE_INGREDIENT_RECIPE_ID =
            "recipe_id";

    public static final String COLUMN_RECIPE_INGREDIENT_NAME =
            "ingredient_name";

    public static final String COLUMN_REQUIRED_QUANTITY =
            "required_quantity";

    public static final String COLUMN_REQUIRED_UNIT =
            "required_unit";

    private static final String CREATE_TABLE_PANTRY_ITEMS =
            "CREATE TABLE " + TABLE_PANTRY_ITEMS + " (" +
                    COLUMN_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
                    COLUMN_QUANTITY + " REAL NOT NULL, " +
                    COLUMN_UNIT + " TEXT NOT NULL, " +
                    COLUMN_EXPIRY_DATE + " TEXT" +
                    ")";

    private static final String CREATE_TABLE_RECIPES =
            "CREATE TABLE " + TABLE_RECIPES + " (" +
                    COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                    COLUMN_PREPARATION_STEPS + " TEXT NOT NULL" +
                    ")";

    private static final String CREATE_TABLE_RECIPE_INGREDIENTS =
            "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                    COLUMN_RECIPE_INGREDIENT_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RECIPE_INGREDIENT_RECIPE_ID +
                    " INTEGER NOT NULL, " +
                    COLUMN_RECIPE_INGREDIENT_NAME +
                    " TEXT NOT NULL, " +
                    COLUMN_REQUIRED_QUANTITY +
                    " REAL NOT NULL, " +
                    COLUMN_REQUIRED_UNIT +
                    " TEXT NOT NULL, " +
                    "FOREIGN KEY (" +
                    COLUMN_RECIPE_INGREDIENT_RECIPE_ID +
                    ") REFERENCES " +
                    TABLE_RECIPES +
                    "(" + COLUMN_RECIPE_ID + ")" +
                    " ON DELETE CASCADE" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PANTRY_ITEMS);
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_RECIPE_INGREDIENTS);

        seedRecipes(db);
    }

    private void seedRecipes(SQLiteDatabase db) {

        long recipeId;

        // 1. Scrambled Eggs
        recipeId = insertRecipe(
                db,
                "Scrambled Eggs",
                "Beat the eggs with milk. Melt butter in a pan, add the eggs and cook gently while stirring until set."
        );
        insertRecipeIngredient(db, recipeId, "Eggs", 2, "pieces");
        insertRecipeIngredient(db, recipeId, "Milk", 30, "ml");
        insertRecipeIngredient(db, recipeId, "Butter", 10, "g");

        // 2. Cheese Omelette
        recipeId = insertRecipe(
                db,
                "Cheese Omelette",
                "Beat the eggs, pour into a heated pan and cook until almost set. Add cheese, fold the omelette and serve."
        );
        insertRecipeIngredient(db, recipeId, "Eggs", 2, "pieces");
        insertRecipeIngredient(db, recipeId, "Cheese", 50, "g");
        insertRecipeIngredient(db, recipeId, "Butter", 10, "g");

        // 3. Pancakes
        recipeId = insertRecipe(
                db,
                "Pancakes",
                "Mix flour, milk and egg into a smooth batter. Cook portions in a lightly greased pan until golden on both sides."
        );
        insertRecipeIngredient(db, recipeId, "Flour", 150, "g");
        insertRecipeIngredient(db, recipeId, "Milk", 250, "ml");
        insertRecipeIngredient(db, recipeId, "Eggs", 1, "piece");

        // 4. Tomato Pasta
        recipeId = insertRecipe(
                db,
                "Tomato Pasta",
                "Cook the pasta. Fry garlic, add chopped tomatoes and simmer. Combine the sauce with the cooked pasta."
        );
        insertRecipeIngredient(db, recipeId, "Pasta", 200, "g");
        insertRecipeIngredient(db, recipeId, "Chopped Tomatoes", 200, "g");
        insertRecipeIngredient(db, recipeId, "Garlic", 2, "cloves");

        // 5. Chicken Rice Bowl
        recipeId = insertRecipe(
                db,
                "Chicken Rice Bowl",
                "Cook the rice. Season and cook the chicken, slice it and serve over the rice."
        );
        insertRecipeIngredient(db, recipeId, "Chicken Breast", 200, "g");
        insertRecipeIngredient(db, recipeId, "Rice", 150, "g");
        insertRecipeIngredient(db, recipeId, "Olive Oil", 15, "ml");

        // 6. Tuna Sandwich
        recipeId = insertRecipe(
                db,
                "Tuna Sandwich",
                "Mix tuna with mayonnaise. Spread onto bread, add lettuce and close the sandwich."
        );
        insertRecipeIngredient(db, recipeId, "Bread", 2, "slices");
        insertRecipeIngredient(db, recipeId, "Tuna", 100, "g");
        insertRecipeIngredient(db, recipeId, "Mayonnaise", 20, "g");
        insertRecipeIngredient(db, recipeId, "Lettuce", 2, "leaves");

        // 7. Grilled Cheese
        recipeId = insertRecipe(
                db,
                "Grilled Cheese",
                "Butter the bread, add cheese between the slices and grill in a pan until golden and the cheese has melted."
        );
        insertRecipeIngredient(db, recipeId, "Bread", 2, "slices");
        insertRecipeIngredient(db, recipeId, "Cheese", 60, "g");
        insertRecipeIngredient(db, recipeId, "Butter", 10, "g");

        // 8. Vegetable Stir-Fry
        recipeId = insertRecipe(
                db,
                "Vegetable Stir-Fry",
                "Heat oil in a pan. Stir-fry the vegetables until tender, add soy sauce and cook for another minute."
        );
        insertRecipeIngredient(db, recipeId, "Carrot", 1, "piece");
        insertRecipeIngredient(db, recipeId, "Bell Pepper", 1, "piece");
        insertRecipeIngredient(db, recipeId, "Broccoli", 100, "g");
        insertRecipeIngredient(db, recipeId, "Soy Sauce", 30, "ml");

        // 9. Baked Potato
        recipeId = insertRecipe(
                db,
                "Baked Potato",
                "Pierce the potato and bake until soft. Cut open, add butter and cheese, then serve."
        );
        insertRecipeIngredient(db, recipeId, "Potato", 1, "piece");
        insertRecipeIngredient(db, recipeId, "Butter", 10, "g");
        insertRecipeIngredient(db, recipeId, "Cheese", 40, "g");

        // 10. Chicken Salad
        recipeId = insertRecipe(
                db,
                "Chicken Salad",
                "Cook and slice the chicken. Combine with lettuce, tomato and cucumber, then drizzle with olive oil."
        );
        insertRecipeIngredient(db, recipeId, "Chicken Breast", 150, "g");
        insertRecipeIngredient(db, recipeId, "Lettuce", 100, "g");
        insertRecipeIngredient(db, recipeId, "Tomato", 1, "piece");
        insertRecipeIngredient(db, recipeId, "Cucumber", 0.5, "piece");
        insertRecipeIngredient(db, recipeId, "Olive Oil", 15, "ml");

        // 11. French Toast
        recipeId = insertRecipe(
                db,
                "French Toast",
                "Beat egg and milk together. Dip the bread into the mixture and cook in a buttered pan until golden on both sides."
        );
        insertRecipeIngredient(db, recipeId, "Bread", 2, "slices");
        insertRecipeIngredient(db, recipeId, "Eggs", 1, "piece");
        insertRecipeIngredient(db, recipeId, "Milk", 50, "ml");
        insertRecipeIngredient(db, recipeId, "Butter", 10, "g");

        // 12. Beef Tacos
        recipeId = insertRecipe(
                db,
                "Beef Tacos",
                "Cook the beef mince until browned. Fill taco shells with beef, lettuce, tomato and cheese."
        );
        insertRecipeIngredient(db, recipeId, "Beef Mince", 200, "g");
        insertRecipeIngredient(db, recipeId, "Taco Shells", 4, "pieces");
        insertRecipeIngredient(db, recipeId, "Lettuce", 50, "g");
        insertRecipeIngredient(db, recipeId, "Tomato", 1, "piece");
        insertRecipeIngredient(db, recipeId, "Cheese", 50, "g");

        // 13. Banana Oatmeal
        recipeId = insertRecipe(
                db,
                "Banana Oatmeal",
                "Cook the oats with milk until thick. Slice the banana and stir it into the cooked oats."
        );
        insertRecipeIngredient(db, recipeId, "Oats", 50, "g");
        insertRecipeIngredient(db, recipeId, "Milk", 250, "ml");
        insertRecipeIngredient(db, recipeId, "Banana", 1, "piece");

        // 14. Garlic Butter Pasta
        recipeId = insertRecipe(
                db,
                "Garlic Butter Pasta",
                "Cook the pasta. Melt butter in a pan, gently fry the garlic and toss through the cooked pasta."
        );
        insertRecipeIngredient(db, recipeId, "Pasta", 200, "g");
        insertRecipeIngredient(db, recipeId, "Butter", 30, "g");
        insertRecipeIngredient(db, recipeId, "Garlic", 3, "cloves");

        // 15. Egg Fried Rice
        recipeId = insertRecipe(
                db,
                "Egg Fried Rice",
                "Cook the egg in a hot pan, add cooked rice and vegetables, then stir in soy sauce and cook until heated through."
        );
        insertRecipeIngredient(db, recipeId, "Rice", 200, "g");
        insertRecipeIngredient(db, recipeId, "Eggs", 2, "pieces");
        insertRecipeIngredient(db, recipeId, "Mixed Vegetables", 100, "g");
        insertRecipeIngredient(db, recipeId, "Soy Sauce", 30, "ml");
    }

    private long insertRecipe(
            SQLiteDatabase db,
            String name,
            String preparationSteps) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, name);
        values.put(COLUMN_PREPARATION_STEPS, preparationSteps);

        return db.insert(TABLE_RECIPES, null, values);
    }

    private void insertRecipeIngredient(
            SQLiteDatabase db,
            long recipeId,
            String ingredientName,
            double quantity,
            String unit) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_INGREDIENT_RECIPE_ID, recipeId);
        values.put(COLUMN_RECIPE_INGREDIENT_NAME, ingredientName);
        values.put(COLUMN_REQUIRED_QUANTITY, quantity);
        values.put(COLUMN_REQUIRED_UNIT, unit);

        db.insert(TABLE_RECIPE_INGREDIENTS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade logic will be added when a future database version requires it.
    }
}