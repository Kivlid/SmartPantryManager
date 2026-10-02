package com.joshuagaillard.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SuggestedRecipesActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";
    public static final String EXTRA_RECIPE_NAME = "recipe_name";

    private DatabaseManager databaseManager;
    private RecipeMatcher recipeMatcher;

    private TextView textSuggestionStatus;
    private LinearLayout layoutRecipeSuggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        recipeMatcher = new RecipeMatcher(databaseManager);

        textSuggestionStatus = findViewById(R.id.textSuggestionStatus);
        layoutRecipeSuggestions = findViewById(R.id.layoutRecipeSuggestions);

        updateSuggestionStatus();

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

    private void updateSuggestionStatus() {

        layoutRecipeSuggestions.removeAllViews();

        Cursor recipesCursor = databaseManager.getAllRecipes();

        int matchingRecipeCount = 0;

        try {
            while (recipesCursor.moveToNext()) {

                long recipeId = recipesCursor.getLong(
                        recipesCursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_RECIPE_ID
                        )
                );

                String recipeName = recipesCursor.getString(
                        recipesCursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_RECIPE_NAME
                        )
                );

                if (recipeMatcher.hasEveryRequiredIngredient(recipeId)) {

                    matchingRecipeCount++;

                    Button recipeButton = new Button(this);
                    recipeButton.setText(recipeName);

                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );

                    layoutParams.setMargins(0, 0, 0, 12);

                    recipeButton.setLayoutParams(layoutParams);

                    recipeButton.setOnClickListener(v ->
                            openRecipeDetail(recipeId, recipeName)
                    );

                    layoutRecipeSuggestions.addView(recipeButton);
                }
            }

        } finally {
            recipesCursor.close();
        }

        if (matchingRecipeCount == 0) {

            textSuggestionStatus.setText(
                    "No recipes can currently be made with the ingredients in your pantry."
            );

        } else {

            textSuggestionStatus.setText(
                    matchingRecipeCount
                            + " recipe(s) can currently be made with your pantry ingredients."
            );
        }
    }

    private void openRecipeDetail(long recipeId, String recipeName) {

        Intent intent = new Intent(
                SuggestedRecipesActivity.this,
                RecipeDetailActivity.class
        );

        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        intent.putExtra(EXTRA_RECIPE_NAME, recipeName);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseManager != null
                && textSuggestionStatus != null
                && layoutRecipeSuggestions != null) {

            updateSuggestionStatus();
        }
    }

    @Override
    protected void onDestroy() {

        if (databaseManager != null) {
            databaseManager.close();
        }

        super.onDestroy();
    }
}