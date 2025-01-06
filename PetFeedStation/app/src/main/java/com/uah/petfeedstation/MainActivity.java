package com.uah.petfeedstation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RadioGroup toggleGroup;
    private RadioButton autoDispenserRadio, manualDispenserRadio;
    private LineChart lineChart;
    private static final long BUTTON_DELAY = 1000; // 1 second delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        /* Texto de bienvenida */
        // Obtener el TextView
        TextView welcomeText = findViewById(R.id.welcome_text);

        // Obtener la fecha actual con el día de la semana en español
        String currentDate = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("es", "ES")).format(new Date());

        // Establecer el texto de bienvenida con la fecha
        welcomeText.setText("¡Hola, usuario!\nHoy es " + currentDate);

        /* Grafica del peso del animal */
        lineChart = findViewById(R.id.lineChart);
        setupLineChart();
        loadChartData();

        /* Botones de dispensador automático y manual */
        Button autoDispenserButton = findViewById(R.id.button_dispenser);
        Button manualDispenserButton = findViewById(R.id.button_manual);

        autoDispenserButton.setOnClickListener(v -> {
            autoDispenserButton.setEnabled(false); // Disable the button
            Intent intent = new Intent(MainActivity.this, AutoDispenserActivity.class);
            startActivity(intent);
            // Re-establecer el botón después de un retraso
            new Handler().postDelayed(() -> autoDispenserButton.setEnabled(true), BUTTON_DELAY);
        });

        manualDispenserButton.setOnClickListener(v -> {
            manualDispenserButton.setEnabled(false); // Disable the button
            Intent intent = new Intent(MainActivity.this, ManualDispenserActivity.class);
            startActivity(intent);
            // Re-establecer el botón después de un retraso
            new Handler().postDelayed(() -> manualDispenserButton.setEnabled(true), BUTTON_DELAY);
        });

        /* Botón de configuración */
        ImageButton settingsButton = findViewById(R.id.button_settings);

        settingsButton.setOnClickListener(v -> {
            settingsButton.setEnabled(false); // Disable the button
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            // Re-establecer el botón después de un retraso
            new Handler().postDelayed(() -> settingsButton.setEnabled(true), BUTTON_DELAY);
        });
    }

    private void setupLineChart() {
        lineChart.getDescription().setEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);
    }

    private void loadChartData() {
        List<Entry> entries = new ArrayList<>();
        // Cargar datos de la base de datos aquí
        // Ejemplo de datos ficticios
        for (int i = 0; i < 30; i++) {
            entries.add(new Entry(i, (float) (Math.random() * 10 + 50)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Peso en los últimos 30 días");
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresca la gráfica
        dataSet.setColor(Color.parseColor("#FFA500")); // Establecer el color de la línea a naranja
        dataSet.setCircleColor(Color.parseColor("#FFA500")); // Establecer el color de los puntos a naranja
    }
}