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

                // IMPLEMENTAR ESTRUCTURA DE DATOS PARA GUARDAR LOS AJUSTES ----------------------------------------------------------------------------
                // Crear un diccionario para guardar los ajustes
                Map<String, Integer> settings = new HashMap<>();
                settings.put("gramsPerPortion", Integer.parseInt(gramsPerPortion));

                // Guardar los ajustes en SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("gramsPerPortion", settings.get("gramsPerPortion"));
                editor.apply();

                // Save the configurations (you can use SharedPreferences or any other method)
                // For demonstration, we will just show a Toast message
                Toast.makeText(SettingsActivity.this, "Settings Saved:\nGrams per Portion: " + gramsPerPortion
                        , Toast.LENGTH_LONG).show();

                // Cerrar la actividad
                finish();
            }
        });
    }

    private void loadSettings() {
        // IMPLEMENTAR CARGA DE AJUSTES DE LA BASE DE DATOS ---------------------------------------------------------------------------------------
        // Cargar los ajustes de la base de datos y mostrarlos en los EditText
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        int gramsPerPortion = sharedPreferences.getInt("gramsPerPortion", 0);

        EditText editGramsPerPortion = findViewById(R.id.edit_grams_per_portion);

        editGramsPerPortion.setText(String.valueOf(gramsPerPortion));
    }
}