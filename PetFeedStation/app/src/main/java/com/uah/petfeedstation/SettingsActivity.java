package com.uah.petfeedstation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /* Botón de retroceso */
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        EditText editGramsPerPortion = findViewById(R.id.edit_grams_per_portion);
        EditText editMaxPortions = findViewById(R.id.edit_max_portions);
        Button buttonSaveSettings = findViewById(R.id.button_save_settings);

        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gramsPerPortion = editGramsPerPortion.getText().toString();
                String maxPortions = editMaxPortions.getText().toString();

                // Validar los gramos por porción
                if (Integer.parseInt(gramsPerPortion) > 250) {
                    Toast.makeText(SettingsActivity.this, "Gramos por porción excedieron los 250 gramos", Toast.LENGTH_LONG).show();
                    return;
                }

                // Validar las porciones máximas
                if (Integer.parseInt(maxPortions) > 10) {
                    Toast.makeText(SettingsActivity.this, "Porciones máximas no pueden exceder las 10 porciones", Toast.LENGTH_LONG).show();
                    return;
                }

                // IMPLEMENTAR ESTRUCTURA DE DATOS PARA GUARDAR LOS AJUSTES ----------------------------------------------------------------------------

                // Save the configurations (you can use SharedPreferences or any other method)
                // For demonstration, we will just show a Toast message
                Toast.makeText(SettingsActivity.this, "Settings Saved:\nGrams per Portion: " + gramsPerPortion
                        + "\nMax Portions: " + maxPortions, Toast.LENGTH_LONG).show();
            }
        });
    }
}