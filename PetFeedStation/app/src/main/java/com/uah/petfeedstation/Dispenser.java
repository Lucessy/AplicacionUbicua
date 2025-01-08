// Dispenser.java
package com.uah.petfeedstation;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Dispenser {
    private static final String TAG = "Dispenser";
    private Mqtt3AsyncClient client;

    public Dispenser(Context context, String serverUri, String username, String password, String topic, String payload) {
        Log.i(TAG, "La ip es esta:"+serverUri.split(":")[1].substring(2));
        client = MqttClient.builder()
                .useMqttVersion3()
                .serverHost(serverUri.split(":")[1].substring(2))
                .serverPort(Integer.parseInt(serverUri.split(":")[2]))
                .buildAsync();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            client.connectWith()
                    .simpleAuth()
                    .username(username)
                    .password(password.getBytes(StandardCharsets.UTF_8))
                    .applySimpleAuth()
                    .send()
                    .whenComplete((connAck, throwable) -> {
                        if (throwable != null) {
                            Log.e(TAG, "Error connecting to MQTT broker", throwable);
                        } else {
                            Log.i(TAG, "Connected to MQTT broker: " + connAck);
                            dispenseFood(topic, payload);

                        }
                    });
        }
    }

    public void dispenseFood(String topic, String payload) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            client.publishWith()
                    .topic(topic)
                    .payload(payload.getBytes(StandardCharsets.UTF_8))
                    .send()
                    .whenComplete((publishResult, throwable) -> {
                        if (throwable != null) {
                            Log.e(TAG, "Error publishing message", throwable);
                        } else {
                            Log.i(TAG, "Message published to topic: " + topic);


                        }
                    });
        }
    }
}