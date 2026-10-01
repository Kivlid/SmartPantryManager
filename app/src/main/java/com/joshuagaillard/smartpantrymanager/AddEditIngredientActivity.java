package com.joshuagaillard.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText editTextIngredientName;
    private EditText editTextQuantity;
    private EditText editTextUnit;
    private EditText editTextExpiryDate;
    private Button buttonSaveIngredient;

    private DatabaseManager databaseManager;

    private long pantryItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_ingredient);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
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

        editTextIngredientName =
                findViewById(R.id.editTextIngredientName);

        editTextQuantity =
                findViewById(R.id.editTextQuantity);

        editTextUnit =
                findViewById(R.id.editTextUnit);

        editTextExpiryDate =
                findViewById(R.id.editTextExpiryDate);

        buttonSaveIngredient =
                findViewById(R.id.buttonSaveIngredient);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        pantryItemId = getIntent().getLongExtra(
                "pantry_item_id",
                -1
        );

        if (pantryItemId != -1) {
            loadPantryItem();
        }

        buttonSaveIngredient.setOnClickListener(v -> saveIngredient());
    }

    private void loadPantryItem() {

        Cursor cursor =
                databaseManager.getPantryItemById(pantryItemId);

        if (cursor != null && cursor.moveToFirst()) {

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

            editTextIngredientName.setText(ingredientName);
            editTextQuantity.setText(String.valueOf(quantity));
            editTextUnit.setText(unit);

            if (expiryDate != null) {
                editTextExpiryDate.setText(expiryDate);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void saveIngredient() {

        String ingredientName =
                editTextIngredientName.getText().toString().trim();

        String quantityText =
                editTextQuantity.getText().toString().trim();

        String unit =
                editTextUnit.getText().toString().trim();

        String expiryDate =
                editTextExpiryDate.getText().toString().trim();

        // P3-07: ingredient name validation
        if (ingredientName.isEmpty()) {

            editTextIngredientName.setError(
                    "Ingredient name is required."
            );

            editTextIngredientName.requestFocus();
            return;
        }

        if (!ingredientName.matches(".*[A-Za-z].*")) {

            editTextIngredientName.setError(
                    "Ingredient name must contain at least one letter."
            );

            editTextIngredientName.requestFocus();
            return;
        }

        // P3-08: quantity validation
        if (quantityText.isEmpty()) {

            editTextQuantity.setError(
                    "Quantity is required."
            );

            editTextQuantity.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);

        } catch (NumberFormatException e) {

            editTextQuantity.setError(
                    "Quantity must be a valid number."
            );

            editTextQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {

            editTextQuantity.setError(
                    "Quantity must be greater than zero."
            );

            editTextQuantity.requestFocus();
            return;
        }

        // P3-09: unit validation
        if (unit.isEmpty()) {

            editTextUnit.setError(
                    "Unit is required."
            );

            editTextUnit.requestFocus();
            return;
        }

        if (!unit.matches("[A-Za-z]+(?:\\s+[A-Za-z]+)*")) {

            editTextUnit.setError(
                    "Enter the unit only, for example kg, g, L, ml or pieces."
            );

            editTextUnit.requestFocus();
            return;
        }

        unit = unit.toLowerCase(Locale.ROOT);

        // P3-10: optional expiry validation
        if (!expiryDate.isEmpty() && !isValidDate(expiryDate)) {

            editTextExpiryDate.setError(
                    "Enter a valid date in YYYY-MM-DD format."
            );

            editTextExpiryDate.requestFocus();
            return;
        }

        String expiryToSave =
                expiryDate.isEmpty() ? null : expiryDate;

        if (pantryItemId == -1) {

            long result = databaseManager.insertPantryItem(
                    ingredientName,
                    quantity,
                    unit,
                    expiryToSave
            );

            if (result != -1) {

                Toast.makeText(
                        this,
                        "Ingredient saved successfully.",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to save ingredient.",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } else {

            int rowsUpdated = databaseManager.updatePantryItem(
                    pantryItemId,
                    ingredientName,
                    quantity,
                    unit,
                    expiryToSave
            );

            if (rowsUpdated > 0) {

                Toast.makeText(
                        this,
                        "Ingredient updated successfully.",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to update ingredient.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private boolean isValidDate(String dateText) {

        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.ROOT
                );

        dateFormat.setLenient(false);

        try {
            Date parsedDate = dateFormat.parse(dateText);

            return parsedDate != null
                    && dateText.equals(
                    dateFormat.format(parsedDate)
            );

        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseManager != null) {
            databaseManager.close();
        }
    }
}