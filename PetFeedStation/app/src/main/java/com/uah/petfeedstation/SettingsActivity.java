package com.uah.petfeedstation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EditText editGramsPerPortion = findViewById(R.id.edit_grams_per_portion);
        RadioGroup radioGroupScheduleType = findViewById(R.id.radio_group_schedule_type);
        EditText editMaxPortions = findViewById(R.id.edit_max_portions);
        Button buttonSaveSettings = findViewById(R.id.button_save_settings);

        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gramsPerPortion = editGramsPerPortion.getText().toString();
                int selectedScheduleTypeId = radioGroupScheduleType.getCheckedRadioButtonId();
                String scheduleType = selectedScheduleTypeId == R.id.radio_24h ? "24h" : "12h";
                String maxPortions = editMaxPortions.getText().toString();

                // Save the configurations (you can use SharedPreferences or any other method)
                // For demonstration, we will just show a Toast message
                Toast.makeText(SettingsActivity.this, "Settings Saved:\nGrams per Portion: " + gramsPerPortion +
                        "\nSchedule Type: " + scheduleType + "\nMax Portions: " + maxPortions, Toast.LENGTH_LONG).show();
            }
        });
    }
}