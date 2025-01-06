package com.uah.petfeedstation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AutoDispenserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_dispenser);

        // Obtener el nombre del día actual
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", new Locale("es", "ES"));
        String dayName = dateFormat.format(calendar.getTime());

        // Actualizar el TextView con el nombre del día
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Plan del " + dayName);

        Button buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar el popup
                Toast.makeText(AutoDispenserActivity.this, "Cambios guardados", Toast.LENGTH_SHORT).show();

                // GUARDAR LOS CAMBIOS AQUI Y ENVIARLOS AL ARDUINO

                // Volver a la pantalla activity_main
                Intent intent = new Intent(AutoDispenserActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

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

        /* Botón de editar */
        ImageButton editButton = findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditMealDialog();
            }
        });
    }

    private void showEditMealDialog() {
        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_meal, null);

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Get the EditText fields from the dialog layout
        EditText editTime = dialogView.findViewById(R.id.edit_time);
        EditText editPortions = dialogView.findViewById(R.id.edit_portions);

        // Set up the dialog buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the user input
                String newTime = editTime.getText().toString();
                String newPortions = editPortions.getText().toString();

                // Update the TextViews with the new values
                TextView timeTextView = findViewById(R.id.time);
                TextView portionsTextView = findViewById(R.id.num_portions);

                timeTextView.setText(newTime);
                portionsTextView.setText(newPortions + " porciones");
            }
        });

        builder.setNegativeButton("Cancel", null);

        // Show the dialog
        builder.create().show();
    }
}