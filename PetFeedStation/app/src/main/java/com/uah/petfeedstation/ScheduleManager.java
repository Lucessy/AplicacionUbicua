package com.uah.petfeedstation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class ScheduleManager {
    public static void scheduleFeeding(Context context, Calendar feedingTime, String topic, String payload) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MqttPublisher.class);
        intent.putExtra("topic", topic);
        intent.putExtra("payload", payload);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                try {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, feedingTime.getTimeInMillis(), pendingIntent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            } else {
                Intent requestPermissionIntent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                context.startActivity(requestPermissionIntent);
            }
        } else {
            try {
                Log.i("ScheduleManager", "Setting alarm for " + feedingTime.getTime());
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, feedingTime.getTimeInMillis(), pendingIntent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public static void cancelFeeding(Context context, String topic, String payload) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MqttPublisher.class);
        intent.putExtra("topic", topic);
        intent.putExtra("payload", payload);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Log.i("ScheduleManager", "Cancelling alarm for " + topic);
        alarmManager.cancel(pendingIntent);
    }
}