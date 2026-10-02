package com.joshuagaillard.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "smart_pantry_settings";
    private static final String KEY_EXPIRY_ALERTS = "expiry_alerts_enabled";

    private Switch switchExpiryAlerts;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);

        sharedPreferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        boolean expiryAlertsEnabled = sharedPreferences.getBoolean(
                KEY_EXPIRY_ALERTS,
                false
        );

        switchExpiryAlerts.setChecked(expiryAlertsEnabled);

        switchExpiryAlerts.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    sharedPreferences
                            .edit()
                            .putBoolean(KEY_EXPIRY_ALERTS, isChecked)
                            .apply();
                }
        );

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
}