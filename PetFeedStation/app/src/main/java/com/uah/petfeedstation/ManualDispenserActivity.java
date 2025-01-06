package com.uah.petfeedstation;

import android.content.DialogInterface;
import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_dispenser);

        /* Botón de retroceso */
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        /* Selección de porciones */
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton buttonPortion = findViewById(R.id.button_portion);
        RadioButton buttonCustom = findViewById(R.id.button_custom);
        LinearLayout portionSelection = findViewById(R.id.portion_selection);
        Spinner spinnerPortions = findViewById(R.id.spinner_portions);
        TextView portionGrams = findViewById(R.id.portion_grams);
        EditText editTextGrams = findViewById(R.id.editText_grams);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.button_portion) {
                    portionSelection.setVisibility(View.VISIBLE);
                    editTextGrams.setVisibility(View.GONE);
                } else if (checkedId == R.id.button_custom) {
                    portionSelection.setVisibility(View.GONE);
                    editTextGrams.setVisibility(View.VISIBLE);
                }
            }
        });

        spinnerPortions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int gramsPerPortion = 50; // Ajusta esto según tu lógica
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
                String gramsText = editTextGrams.getVisibility() == View.VISIBLE ? editTextGrams.getText().toString() : portionGrams.getText().toString();
                new AlertDialog.Builder(ManualDispenserActivity.this)
                        .setTitle("Confirmar dispensación")
                        .setMessage("¿Está seguro de dispensar " + gramsText + "?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Volver a la pantalla activity_main
                                Intent intent = new Intent(ManualDispenserActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }
}