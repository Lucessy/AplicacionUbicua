package com.uah.petfeedstation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainMenuActivity extends AppCompatActivity {
    private LineChart lineChart;
    private static final long BUTTON_DELAY = 1000; // 1 second delay
    private int dispensedFood = 0;
    private float foodRemaining = 0;
    private List<Entry> entriesPetWeight;
    private String tag = "MainMenuActivity";
    private String currentID = "";
    private String ipVirtualMachine = "192.168.1.35";
    private SaveSettingsActivity saveSettingsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);

        /* Recuperar el registerID de la actividad anterior */
        if (savedInstanceState != null) {
            currentID = savedInstanceState.getString("currentID");
            ipVirtualMachine = savedInstanceState.getString("ipVirtualMachine");
        } else {
            currentID = getIntent().getStringExtra("registerID");
            ipVirtualMachine = getIntent().getStringExtra("ipVirtualMachine");
        }
        /* Texto de bienvenida */
        TextView welcomeText = findViewById(R.id.welcome_text);
        String currentDate = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("es", "ES")).format(new Date());
        welcomeText.setText("¡Hola, usuario!\nHoy es " + currentDate);

        /* Guardar configuración del dispositivo */
        saveSettingsActivity = new SaveSettingsActivity(this);
        // Verificar si la configuración ya existe antes de guardarla
        if (saveSettingsActivity.getSetting("gramsPerPortion") == null) {
            saveSettingsActivity.saveSetting("gramsPerPortion", "25"); // Valor por defecto
        }
        // Recuperar una configuración
        String value = saveSettingsActivity.getSetting("gramsPerPortion");
        if (value != null) {
            Toast.makeText(this, "Valor de configuración: " + value, Toast.LENGTH_LONG).show();
        }

        /* Comida dispensada */

        loadDispensedFood();
        updateDispensedFood();

        /* Comida restante */
        loadFoodRemaining();
        updateFoodRemaining();

        /* Grafica del peso del animal */
        lineChart = findViewById(R.id.lineChart);
        setupLineChart();
        loadChartData();


        /* Botones de dispensador automático y manual */
        Button autoDispenserButton = findViewById(R.id.button_dispenser);
        Button manualDispenserButton = findViewById(R.id.button_manual);

        autoDispenserButton.setOnClickListener(v -> {
            autoDispenserButton.setEnabled(false); // Disable the button
            Intent intent = new Intent(MainMenuActivity.this, AutoDispenserActivity.class);

            intent.putExtra("registerID", currentID); // Enviar el registerID a la siguiente actividad
            intent.putExtra("ipVirtualMachine", ipVirtualMachine); // Enviar la IP de la máquina virtual

            startActivity(intent);
            // Re-establecer el botón después de un retraso
            new Handler().postDelayed(() -> autoDispenserButton.setEnabled(true), BUTTON_DELAY);
        });

        manualDispenserButton.setOnClickListener(v -> {
            manualDispenserButton.setEnabled(false); // Disable the button
            Intent intent = new Intent(MainMenuActivity.this, ManualDispenserActivity.class);

            intent.putExtra("registerID", currentID); // Enviar el registerID a la siguiente actividad
            intent.putExtra("ipVirtualMachine", ipVirtualMachine); // Enviar la IP de la máquina virtual

            startActivity(intent);
            // Re-establecer el botón después de un retraso
            new Handler().postDelayed(() -> manualDispenserButton.setEnabled(true), BUTTON_DELAY);
        });

        /* Botón de configuración */
        ImageButton settingsButton = findViewById(R.id.button_settings);

        settingsButton.setOnClickListener(v -> {
            settingsButton.setEnabled(false); // Disable the button
            Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
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
        loadPetWeight();
        int sizeHistory = entriesPetWeight.size();

        LineDataSet dataSet = new LineDataSet(entriesPetWeight, "Peso en los últimos " + sizeHistory + " días");
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresca la gráfica
        dataSet.setColor(Color.parseColor("#FFA500")); // Establecer el color de la línea a naranja
        dataSet.setCircleColor(Color.parseColor("#FFA500")); // Establecer el color de los puntos a naranja
    }

    private void loadPetWeight(){
        String url = "http://" + ipVirtualMachine + ":8080/EstacionComidaServer/GetHistorialPesoID" + "?id=" + currentID;
        entriesPetWeight = new ArrayList<>();
        ServerConnectionThread thread = new ServerConnectionThread(this, url);
        try {
            thread.join();
        }catch (InterruptedException e){}
    }

    public void setEntriesPetWeight(JSONArray jsonArray) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy, h:mm:ss a", Locale.ENGLISH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                String dateStr = jsonobject.getString("fecha");
                Date date = dateFormat.parse(dateStr);
                int day = Integer.parseInt(new SimpleDateFormat("d", Locale.ENGLISH).format(date));
                float value = (float) jsonobject.getDouble("valor");
                entriesPetWeight.add(new Entry(day, value));
            }
        } catch (Exception e) {
            Log.e(tag, "Error: " + e);
        }
    }

    private void loadDispensedFood() {
        String url = "http://" + ipVirtualMachine + ":8080/EstacionComidaServer/GetHistorialDispensacionID" + "?id=" + currentID;
        ServerConnectionThread thread = new ServerConnectionThread(this, url);
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
    }

    public void setDispensedFood(JSONArray jsonArray) {
        try {

                JSONObject jsonobject = jsonArray.getJSONObject(0);
                dispensedFood = jsonobject.getInt("valor");

        } catch (Exception e) {
            Log.e(tag, "Error: " + e);
        }
    }

    private void updateDispensedFood() {
        TextView foodDispensedText = findViewById(R.id.food_dispensed);
        foodDispensedText.setText(dispensedFood + " gramos");
    }

    private void loadFoodRemaining() {
        String url = "http://" + ipVirtualMachine + ":8080/EstacionComidaServer/GetEstacionComida" + "?id=" + currentID;
        ServerConnectionThread thread = new ServerConnectionThread(this, url);
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
    }

    public void setFoodRemaining(JSONArray jsonArray) {
        try {

                JSONObject jsonobject = jsonArray.getJSONObject(0);
                foodRemaining = jsonobject.getInt("distancia");

        } catch (Exception e) {
            Log.e(tag, "Error: " + e);
        }
    }

    private void updateFoodRemaining() {
        TextView foodRemainingText = findViewById(R.id.food_percentage);
        foodRemainingText.setText(foodRemaining + " %");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentID", currentID);
        outState.putString("ipVirtualMachine", ipVirtualMachine);
    }

}
