// AlarmReceiver.java
package com.uah.petfeedstation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String time = intent.getStringExtra("time");
        String portions = intent.getStringExtra("portions");

        // Handle the logic to dispense food
        Toast.makeText(context, "Dispensando: " + portions + " porciones a las " + time, Toast.LENGTH_SHORT).show();
    }
}
