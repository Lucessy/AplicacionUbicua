package com.uah.petfeedstation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private SaveSettingsActivity saveSettingsActivity;
    private int gramsPerPortion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        loadSettings();

        /* Botón de retroceso */
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        EditText editGramsPerPortion = findViewById(R.id.edit_grams_per_portion);
        Button buttonSaveSettings = findViewById(R.id.button_save_settings);

        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gramsPerPortion = editGramsPerPortion.getText().toString();

                // Validar los gramos por porción
                if (Integer.parseInt(gramsPerPortion) > 250 ) {
                    Toast.makeText(SettingsActivity.this, "Gramos por porción excedieron los 250 gramos", Toast.LENGTH_LONG).show();
                    return;
                }

                if (Integer.parseInt(gramsPerPortion) <= 0 ) {
                    Toast.makeText(SettingsActivity.this, "Gramos por porción no pueden ser 0", Toast.LENGTH_LONG).show();
                    return;
                }

                // Guardar la configuración
                saveSettingsActivity.saveSetting("gramsPerPortion", gramsPerPortion);

                Toast.makeText(SettingsActivity.this, "Configuración guardada:\nGramos por porción: " + gramsPerPortion
                        , Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }

    private void loadSettings() {
        /* Actualización de gramos por porción */
        saveSettingsActivity = new SaveSettingsActivity(this);
        // Recuperar una configuración
        gramsPerPortion = Integer.parseInt(saveSettingsActivity.getSetting("gramsPerPortion"));

        EditText editGramsPerPortion = findViewById(R.id.edit_grams_per_portion);
        editGramsPerPortion.setText(String.valueOf(gramsPerPortion));
    }
}