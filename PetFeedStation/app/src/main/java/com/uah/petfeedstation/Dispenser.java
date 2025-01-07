// Dispenser.java
package com.uah.petfeedstation;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Dispenser {

    private MqttAndroidClient client;
    private static final String TAG = "Dispenser";

    public Dispenser(Context context, String serverUri, String username, String password) {
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(context, serverUri, clientId);

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "MQTT connected");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "Error connecting MQTT: " + exception);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void dispenseFood(String topic, String payload) {
        try {
            MqttMessage message = new MqttMessage(payload.getBytes("UTF-8"));
            client.publish(topic, message);
        } catch (Exception e) {
            Log.e(TAG, "Error publishing MQTT message: " + e);
        }
    }
}