// SaveMealsActivity.java
package com.uah.petfeedstation;

import android.content.Context;
import android.content.SharedPreferences;

import com.uah.petfeedstation.data.Meal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SaveMealsActivity {
    private static final String PREFS_NAME = "MealsPrefs";
    private SharedPreferences sharedPreferences;

    public SaveMealsActivity(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveMeals(List<Meal> meals) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonArray = new JSONArray();
        for (Meal meal : meals) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("time", meal.getTime());
                jsonObject.put("portions", meal.getPortions());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        editor.putString("meals", jsonArray.toString());
        editor.apply();
    }

    public List<Meal> getMeals() {
        List<Meal> meals = new ArrayList<>();
        String mealsString = sharedPreferences.getString("meals", null);
        if (mealsString != null) {
            try {
                JSONArray jsonArray = new JSONArray(mealsString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String time = jsonObject.getString("time");
                    String portions = jsonObject.getString("portions");
                    meals.add(new Meal(time, portions));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return meals;
    }
}