package com.uah.petfeedstation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ManualDispenserActivity extends AppCompatActivity {

    private int foodRemaining = 0;
    private int gramsToSend = 0;
    private int gramsPerPortion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_dispenser);

        /* Botón de retroceso */
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        /* Cantidad de comida restante */
        // Cargar la cantidad de comida restante de la base de datos aquí ---------------------------------------------------------------
        TextView foodRemainingTextView = findViewById(R.id.food_remaining);
        foodRemainingTextView.setText(foodRemaining + " gramos");

        /* Actualización de gramos por porción */
        // Cargar la cantidad de gramos por porción de la base de datos aquí ---------------------------------------------------------------
        //gramsPerPortion = 50; // Se obtiene de la configuración de la base de datos
        // Por el momento usamos un valor fijo
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        gramsPerPortion = sharedPreferences.getInt("gramsPerPortion", 0);

        /* Selección de porciones */
        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        LinearLayout portionCustom = findViewById(R.id.portion_custom);
        EditText editTextGrams = findViewById(R.id.editText_grams);

        LinearLayout portionSelection = findViewById(R.id.portion_selection);
        Spinner spinnerPortions = findViewById(R.id.spinner_portions);
        TextView portionGrams = findViewById(R.id.portion_grams);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.button_portion) {
                    portionSelection.setVisibility(View.VISIBLE);
                    portionCustom.setVisibility(View.GONE);

                } else if (checkedId == R.id.button_custom) {
                    portionSelection.setVisibility(View.GONE);
                    portionCustom.setVisibility(View.VISIBLE);
                }
            }
        });

        spinnerPortions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedPortions = position + 1;
                int totalGrams = selectedPortions * gramsPerPortion;
                portionGrams.setText(totalGrams + " gramos");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                portionGrams.setText("0 gramos");
            }
        });

        /* Botón para dispensar comida */
        Button buttonDispense = findViewById(R.id.button_dispense);
        buttonDispense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gramsText = portionCustom.getVisibility() == View.VISIBLE ? editTextGrams.getText().toString() : portionGrams.getText().toString();
                try {
                    int gramsToDispense = Integer.parseInt(gramsText.split(" ")[0]);

                    if (gramsToDispense <= 0) {
                        throw new NumberFormatException();
                    } else

                    if (gramsToDispense > foodRemaining) {
                        new AlertDialog.Builder(ManualDispenserActivity.this)
                                .setTitle("Error")
                                .setMessage("No hay suficiente comida en el tanque para dispensar " + gramsToDispense + " gramos.")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(ManualDispenserActivity.this)
                                .setTitle("Confirmar dispensación")
                                .setMessage("¿Está seguro de dispensar " + gramsText + "?")
                                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Volver a la pantalla activity_main
                                        gramsToSend = gramsToDispense;
                                        // Actualizar y enviar la cantidad de comida restante en la base de datos aquí ---------------------------------------------------------------

                                        Intent intent = new Intent(ManualDispenserActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }

                } catch (NumberFormatException e) {
                    new AlertDialog.Builder(ManualDispenserActivity.this)
                            .setTitle("Error")
                            .setMessage("Por favor, ingrese un número de gramos válido.")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }
        });
    }
}