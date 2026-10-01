package com.joshuagaillard.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private RecipeMatcher recipeMatcher;

    private TextView textSuggestionStatus;
    private Button buttonRecipeDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        recipeMatcher = new RecipeMatcher(databaseManager);

        textSuggestionStatus = findViewById(R.id.textSuggestionStatus);
        buttonRecipeDetail = findViewById(R.id.buttonRecipeDetail);

        buttonRecipeDetail.setOnClickListener(v ->
                testScrambledEggsMatch()
        );

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

        Cursor recipesCursor = databaseManager.getAllRecipes();

        int matchingRecipeCount = 0;

        try {
            while (recipesCursor.moveToNext()) {

                long recipeId = recipesCursor.getLong(
                        recipesCursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_RECIPE_ID
                        )
                );

                if (recipeMatcher.hasEveryRequiredIngredient(recipeId)) {
                    matchingRecipeCount++;
                }
            }

        } finally {
            recipesCursor.close();
        }

        if (matchingRecipeCount == 0) {

            textSuggestionStatus.setText(
                    "No recipes can currently be made with the ingredients in your pantry."
            );

            buttonRecipeDetail.setVisibility(View.GONE);

        } else {

            textSuggestionStatus.setText(
                    matchingRecipeCount
                            + " recipe(s) can currently be made with your pantry ingredients."
            );

            buttonRecipeDetail.setVisibility(View.VISIBLE);
        }
    }

    private void testScrambledEggsMatch() {

        Cursor recipesCursor = databaseManager.getAllRecipes();

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

                if ("Scrambled Eggs".equalsIgnoreCase(recipeName)) {

                    boolean matches =
                            recipeMatcher.hasEveryRequiredIngredient(recipeId);

                    String message;

                    if (matches) {
                        message = "MATCH: Scrambled Eggs";
                    } else {
                        message = "NO MATCH: Scrambled Eggs";
                    }

                    Toast.makeText(
                            this,
                            message,
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                }
            }

            Toast.makeText(
                    this,
                    "Scrambled Eggs recipe not found.",
                    Toast.LENGTH_LONG
            ).show();

        } finally {
            recipesCursor.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseManager != null
                && textSuggestionStatus != null) {

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