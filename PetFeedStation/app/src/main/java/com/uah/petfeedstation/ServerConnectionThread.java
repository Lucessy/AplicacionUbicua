// ServerConnectionThread.java
package com.uah.petfeedstation;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class ServerConnectionThread extends Thread {
    private Activity activity;
    private String tag = "ServerConnectionThread";
    private String urlStr = "";

    public ServerConnectionThread(Activity activity, String url) {
        this.activity = activity;
        this.urlStr = url;
        Log.i(tag, "ServerConnectionThread started");
        start();
    }

    @Override
    public void run() {
        String response = "";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            response = convertStreamToString(in);
            Log.d(tag, "get json: " + response);
            JSONArray jsonarray = new JSONArray(response);

            /* Main Activity */
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).onServerResponse(jsonarray);

            } /* Main Menu Activity */
            else if (activity instanceof MainMenuActivity) {

                if (urlStr.contains("GetHistorialDispensacionID")) {
                    ((MainMenuActivity) activity).setDispensedFood(jsonarray);
                } else if (urlStr.contains("GetHistorialPesoID")) {
                    ((MainMenuActivity) activity).setEntriesPetWeight(jsonarray);
                }else if (urlStr.contains("GetEstacionComida")) {
                    ((MainMenuActivity) activity).setFoodRemaining(jsonarray);
                }

            } /* Manual Dispenser Activity */
            else if (activity instanceof ManualDispenserActivity) {
                ((ManualDispenserActivity) activity).setFoodRemaining(jsonarray);
            } /* Automatic Dispenser Activity */

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e(tag, "Error: " + e.getMessage());
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}