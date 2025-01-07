package com.uah.petfeedstation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private String tag = "MainActivity";
    private String registerID = "";
    private String ipVirtualMachine = "192.168.1.35";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        EditText editRegisterID = findViewById(R.id.edit_register_id);
        editRegisterID.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editRegisterID.setText("");
            }
        });

        // Cargar datos según identificador de usuario
        Button buttonMainMenu = findViewById(R.id.button_login);
        buttonMainMenu.setOnClickListener(v -> {

            // Verificar si el usuario está registrado
            this.registerID = editRegisterID.getText().toString();
            verifyUser();

        });
    }

    private void verifyUser() {
        // Verificar si el usuario está registrado
        Log.i(tag, "Verifying user...");
        String url = "http://" + this.ipVirtualMachine + ":8080/EstacionComidaServer/ComprobarId" + "?id=" + this.registerID;
        ServerConnectionThread thread = new ServerConnectionThread(this, url);
        try {
            thread.join();
        }catch (InterruptedException e){}
    }

    public void onServerResponse(JSONArray jsonarray) {
        String userRegistered = "false";
        Log.i(tag, "Server response: " + jsonarray.toString());
        try {
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                userRegistered = jsonobject.getString("respuesta");
            }
        }catch (Exception e){
            Log.e(tag,"Error: " + e);
        }

        // Procesar la respuesta del servidor
        Log.i(tag, "User registered: " + userRegistered);
        if (userRegistered.equals("true")) {
            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
            intent.putExtra("registerID", this.registerID); // Enviar el registerID a la siguiente actividad
            intent.putExtra("ipVirtualMachine", this.ipVirtualMachine); // Enviar la IP de la máquina virtual
            startActivity(intent);
        } else {
            // Mostrar mensaje de error
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(MainActivity.this, "Respuesta del servidor recibida", Toast.LENGTH_LONG).show();
            });
        }
    }
}