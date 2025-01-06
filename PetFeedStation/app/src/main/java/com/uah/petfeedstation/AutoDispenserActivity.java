package com.uah.petfeedstation;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AutoDispenserActivity extends AppCompatActivity {

    private int numTotalMeals = 0; // Número total de comidas
    private int numTotalPortions = 0; // Número de porciones
    private int gramsPerPortion = 0; // Gramos por porción
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_dispenser);

        /* Titulo de la barra de herramientas */
        // Obtener el nombre del día actual
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", new Locale("es", "ES"));
        String dayName = dateFormat.format(calendar.getTime());
        // Actualizar el TextView con el nombre del día
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Plan del " + dayName);

        /* Actualizar valores */
        // Obtener los valores de la base de datos aquí ---------------------------------------------------------------
        gramsPerPortion = 50; // Gramos por porción
        numTotalMeals = 3; // Número total de comidas
        numTotalPortions = 5; // Número de porciones

        // Actualizar los TextViews con los valores obtenidos
        TextView numMealsTextView = findViewById(R.id.num_meals_total);
        numMealsTextView.setText(String.valueOf(numTotalMeals));

        TextView numPortionsTextView = findViewById(R.id.num_portions_total);
        numPortionsTextView.setText(String.valueOf(numTotalPortions));

        TextView gramsPerPortionTextView = findViewById(R.id.num_total_grams);
        gramsPerPortionTextView.setText(String.valueOf(gramsPerPortion * numTotalPortions));

        /* Botón de guardar */
        Button buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar el popup
                Toast.makeText(AutoDispenserActivity.this, "Cambios guardados", Toast.LENGTH_SHORT).show();

                // GUARDAR LOS CAMBIOS AQUI Y ENVIARLOS AL ARDUINO ---------------------------------------------------------------

                // Volver a la pantalla activity_main
                Intent intent = new Intent(AutoDispenserActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /* Botón de ver calendario POR VERIFICAR SI ES NECESARIO */
        Button buttonViewCalendar = findViewById(R.id.button_view_calendar);
        buttonViewCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the calendar and select a date
                Calendar selectedDate = Calendar.getInstance(); // Replace with actual date selection logic
                Intent intent = new Intent(AutoDispenserActivity.this, ViewPlanActivity.class);
                intent.putExtra("selectedDate", selectedDate.getTimeInMillis());
                startActivity(intent);
            }
        });

        /* Botón de añadir */
        ImageButton addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add a new meal entry with default values
                addMealEntry("08:00", "1");
            }
        });

        /* Botón de atrás */
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volver a la pantalla activity_main
                Intent intent = new Intent(AutoDispenserActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showEditMealDialog(View mealEntryView, String currentTime, String currentPortions) {
        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_meal, null);

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Get the EditText fields from the dialog layout
        EditText editPortions = dialogView.findViewById(R.id.edit_portions);
        TextView editTime = dialogView.findViewById(R.id.edit_time);

        // Set the current values
        editTime.setText(currentTime);
        editPortions.setText(currentPortions);

        // Set up the time picker dialog
        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AutoDispenserActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                editTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        // Set up the dialog buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the user input
                String newTime = editTime.getText().toString();
                String newPortions = editPortions.getText().toString();

                // Update the meal entry
                TextView timeTextView = mealEntryView.findViewById(R.id.time);
                TextView portionsTextView = mealEntryView.findViewById(R.id.num_portions);
                timeTextView.setText(newTime);
                portionsTextView.setText(newPortions + " porciones");
            }
        });

        builder.setNegativeButton("Cancel", null);

        // Show the dialog
        builder.create().show();
    }

    private void addMealEntry(String time, String portions) {
        LinearLayout scrollViewLayout = findViewById(R.id.scroll_view_layout);

        // Inflate the new meal entry layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View newMealEntry = inflater.inflate(R.layout.meal_entry, null);

        // Set the time and portions
        TextView timeTextView = newMealEntry.findViewById(R.id.time);
        TextView portionsTextView = newMealEntry.findViewById(R.id.num_portions);
        timeTextView.setText(time);
        portionsTextView.setText(portions + " porciones");

        // Set the edit button click listener
        ImageButton editButton = newMealEntry.findViewById(R.id.edit_button_meal);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditMealDialog(newMealEntry, time, portions);
            }
        });

        // Set the delete button click listener
        ImageButton deleteButton = newMealEntry.findViewById(R.id.delete_button_meal);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollViewLayout.removeView(newMealEntry);
            }
        });

        // Add the new meal entry to the scroll view layout
        scrollViewLayout.addView(newMealEntry);
    }
}