package com.joshuagaillard.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private ListView listViewPantryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        listViewPantryItems = findViewById(R.id.listViewPantryItems);

        loadPantryItems();

        // Normal tap = edit item
        listViewPantryItems.setOnItemClickListener(
                (parent, view, position, id) -> {

                    PantryItem selectedItem =
                            (PantryItem) parent.getItemAtPosition(position);

                    Intent intent = new Intent(
                            MainActivity.this,
                            AddEditIngredientActivity.class
                    );

                    intent.putExtra(
                            "pantry_item_id",
                            selectedItem.getId()
                    );

                    startActivity(intent);
                }
        );

        // Long press = delete item
        listViewPantryItems.setOnItemLongClickListener(
                (parent, view, position, id) -> {

                    PantryItem selectedItem =
                            (PantryItem) parent.getItemAtPosition(position);

                    showDeleteConfirmation(selectedItem);

                    return true;
                }
        );

        findViewById(R.id.buttonAddIngredient).setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        findViewById(R.id.buttonSuggestedRecipes).setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipesActivity.class
            );

            startActivity(intent);
        });

        findViewById(R.id.buttonSettings).setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);
        });

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

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseManager != null) {
            loadPantryItems();
        }
    }

    private void showDeleteConfirmation(PantryItem pantryItem) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage(
                        "Are you sure you want to delete "
                                + pantryItem.getIngredientName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> deletePantryItem(pantryItem)
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .show();
    }

    private void deletePantryItem(PantryItem pantryItem) {

        int rowsDeleted =
                databaseManager.deletePantryItem(
                        pantryItem.getId()
                );

        if (rowsDeleted > 0) {

            Toast.makeText(
                    this,
                    "Ingredient deleted successfully.",
                    Toast.LENGTH_SHORT
            ).show();

            loadPantryItems();

        } else {

            Toast.makeText(
                    this,
                    "Failed to delete ingredient.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void loadPantryItems() {

        ArrayList<PantryItem> pantryItems = new ArrayList<>();

        Cursor cursor = databaseManager.getAllPantryItems();

        if (cursor != null) {

            while (cursor.moveToNext()) {

                long id = cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_PANTRY_ID
                        )
                );

                String ingredientName = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_INGREDIENT_NAME
                        )
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_QUANTITY
                        )
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_UNIT
                        )
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COLUMN_EXPIRY_DATE
                        )
                );

                pantryItems.add(
                        new PantryItem(
                                id,
                                ingredientName,
                                quantity,
                                unit,
                                expiryDate
                        )
                );
            }

            cursor.close();
        }

        PantryItemAdapter adapter =
                new PantryItemAdapter(
                        this,
                        pantryItems
                );

        listViewPantryItems.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {

        if (databaseManager != null) {
            databaseManager.close();
        }

        super.onDestroy();
    }
}