package com.joshuagaillard.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipeDetailActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;

    private TextView textRecipeName;
    private TextView textRecipeIngredients;
    private TextView textPreparationSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);

        textRecipeName = findViewById(R.id.textRecipeName);
        textRecipeIngredients = findViewById(R.id.textRecipeIngredients);
        textPreparationSteps = findViewById(R.id.textPreparationSteps);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        long recipeId = getIntent().getLongExtra(
                SuggestedRecipesActivity.EXTRA_RECIPE_ID,
                -1
        );

        String recipeName = getIntent().getStringExtra(
                SuggestedRecipesActivity.EXTRA_RECIPE_NAME
        );

        if (recipeId == -1 || recipeName == null || recipeName.trim().isEmpty()) {

            textRecipeName.setText("Recipe not found");
            textRecipeIngredients.setText("No ingredients available.");
            textPreparationSteps.setText("No preparation steps available.");

        } else {

            textRecipeName.setText(recipeName);

            loadRecipeIngredients(recipeId);
            loadPreparationSteps(recipeId);
        }

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );
    }

    private void loadRecipeIngredients(long recipeId) {

        Cursor ingredientsCursor =
                databaseManager.getRecipeIngredients(recipeId);

        StringBuilder ingredientsText = new StringBuilder();

        try {
            while (ingredientsCursor.moveToNext()) {

                String ingredientName = ingredientsCursor.getString(
                        ingredientsCursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_RECIPE_INGREDIENT_NAME
                        )
                );

                double requiredQuantity = ingredientsCursor.getDouble(
                        ingredientsCursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_REQUIRED_QUANTITY
                        )
                );

                String requiredUnit = ingredientsCursor.getString(
                        ingredientsCursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_REQUIRED_UNIT
                        )
                );

                ingredientsText
                        .append("• ")
                        .append(formatQuantity(requiredQuantity))
                        .append(" ")
                        .append(requiredUnit)
                        .append(" ")
                        .append(ingredientName)
                        .append("\n");
            }

        } finally {
            ingredientsCursor.close();
        }

        if (ingredientsText.length() == 0) {

            textRecipeIngredients.setText("No ingredients available.");

        } else {

            textRecipeIngredients.setText(
                    ingredientsText.toString().trim()
            );
        }
    }

    private void loadPreparationSteps(long recipeId) {

        Cursor recipesCursor = databaseManager.getAllRecipes();

        String preparationSteps = null;

        try {
            while (recipesCursor.moveToNext()) {

                long currentRecipeId = recipesCursor.getLong(
                        recipesCursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_RECIPE_ID
                        )
                );

                if (currentRecipeId == recipeId) {

                    preparationSteps = recipesCursor.getString(
                            recipesCursor.getColumnIndexOrThrow(
                                    DatabaseHelper.COLUMN_PREPARATION_STEPS
                            )
                    );

                    break;
                }
            }

        } finally {
            recipesCursor.close();
        }

        if (preparationSteps == null || preparationSteps.trim().isEmpty()) {

            textPreparationSteps.setText(
                    "No preparation steps available."
            );

        } else {

            textPreparationSteps.setText(preparationSteps);
        }
    }

    private String formatQuantity(double quantity) {

        if (quantity == Math.floor(quantity)) {
            return String.valueOf((long) quantity);
        }

        return String.valueOf(quantity);
    }

    @Override
    protected void onDestroy() {

        if (databaseManager != null) {
            databaseManager.close();
        }

        super.onDestroy();
    }
}