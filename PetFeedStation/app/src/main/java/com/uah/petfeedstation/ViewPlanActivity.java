package com.uah.petfeedstation;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ViewPlanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plan);

        // Get the selected date from the intent
        long dateInMillis = getIntent().getLongExtra("selectedDate", -1);
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(dateInMillis);

        // Format the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate.getTime());

        // Display the date
        /*
        TextView dateTextView = findViewById(R.id.date_text_view);
        dateTextView.setText(formattedDate);
        */
        // Check if the date is in the past
        Calendar today = Calendar.getInstance();
        if (selectedDate.before(today)) {
            // Disable editing
            // Add your logic to disable editing here
        } else {
            // Enable editing
            // Add your logic to enable editing here
        }
    }
}