package com.uah.petfeedstation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ManualDispenserActivity extends AppCompatActivity {

    private int foodRemainingBowl = 0;
    private int gramsToSend = 0;
    private int gramsPerPortion = 0;
    private float percentageFoodRemaining = 0;
    private SaveSettingsActivity saveSettingsActivity;
    private String currentID = "";
    private String ipVirtualMachine = "";
    private String tag = "ManualDispenserActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_dispenser);

        /* Recuperar el registerID de la actividad anterior */
        currentID = getIntent().getStringExtra("registerID");
        ipVirtualMachine = getIntent().getStringExtra("ipVirtualMachine");

        /* Botón de retroceso */
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        /* Cantidad de comida restante */
        loadFoodRemaining();
        updateFoodRemaining();

        /* Actualización de gramos por porción */
        saveSettingsActivity = new SaveSettingsActivity(this);
        // Recuperar una configuración
        gramsPerPortion = Integer.parseInt(saveSettingsActivity.getSetting("gramsPerPortion"));

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

                    if (percentageFoodRemaining == 0) {
                        new AlertDialog.Builder(ManualDispenserActivity.this)
                                .setTitle("Error")
                                .setMessage("No hay suficiente comida en el tanque para dispensar ")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(ManualDispenserActivity.this)
                                .setTitle("Confirmar dispensación")
                                .setMessage("¿Está seguro de dispensar " + gramsToDispense + " gramos?")
                                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Dispenser dispenser = new Dispenser(ManualDispenserActivity.this, "tcp://192.168.1.35:1883", "ubicua", "ubicua",   currentID + "/dispensar",String.valueOf(gramsToDispense) );
                                        dispenser.publishMessage();
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

    private void updateFoodRemaining() {
        TextView foodRemainingTextView = findViewById(R.id.food_remaining);
        foodRemainingTextView.setText(percentageFoodRemaining + " %");

        TextView foodRemainingBowlTextView = findViewById(R.id.food_remaining_bowl);
        foodRemainingBowlTextView.setText(foodRemainingBowl + " gramos");
    }

    private void loadFoodRemaining() {
        String url = "http://" + ipVirtualMachine + ":8080/EstacionComidaServer/GetEstacionComida" + "?id=" + currentID;
        ServerConnectionThread thread = new ServerConnectionThread(this, url);
        try {
            thread.join();
        }catch (InterruptedException e){}
    }

    public void setFoodRemaining(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                foodRemainingBowl = jsonobject.getInt("peso");
                percentageFoodRemaining = (float) jsonobject.getDouble("distancia");
            }
        } catch (Exception e) {
            Log.e(tag, "Error: " + e);
        }
    }
}