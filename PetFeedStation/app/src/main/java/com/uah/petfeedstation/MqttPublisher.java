// MqttPublisher.java
package com.uah.petfeedstation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttPublisher extends BroadcastReceiver {
    private static final String TAG = "MqttPublisher";

    @Override
    public void onReceive(Context context, Intent intent) {
        String topic = intent.getStringExtra("topic");
        String payload = intent.getStringExtra("payload");

        Dispenser dispenser = new Dispenser(context, "tcp://192.168.1.35:1883", "ubicua", "ubicua", topic, payload);
        dispenser.publishMessage();
    }
}
