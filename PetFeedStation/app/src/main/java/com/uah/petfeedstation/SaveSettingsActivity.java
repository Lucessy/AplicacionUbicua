// SaveSettingsActivity.java
package com.uah.petfeedstation;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveSettingsActivity {

    private static final String PREFS_NAME = "PetFeedStationPrefs";
    private SharedPreferences sharedPreferences;

    public SaveSettingsActivity(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveSetting(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getSetting(String key) {
        return sharedPreferences.getString(key, null);
    }
}