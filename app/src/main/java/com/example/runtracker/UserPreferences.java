package com.example.runtracker;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    private static final String PREFS_NAME = "sprint_user_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_JOIN_YEAR = "join_year";
    private static final String KEY_WEIGHT_KG = "weight_kg";
    private static final String KEY_HEIGHT_CM = "height_cm";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PHOTO_PATH = "photo_path";
    private static final String DEFAULT_USERNAME = "Invitado";

    private final SharedPreferences prefs;

    public UserPreferences(Context context) {
        this.prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, DEFAULT_USERNAME);
    }

    public void setUsername(String username) {
        prefs.edit().putString(KEY_USERNAME, username).apply();
    }

    public int getJoinYear() {
        int current = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        return prefs.getInt(KEY_JOIN_YEAR, current);
    }

    public void initializeJoinYearIfNeeded() {
        if (!prefs.contains(KEY_JOIN_YEAR)) {
            int current = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
            prefs.edit().putInt(KEY_JOIN_YEAR, current).apply();
        }
    }

    
    public float getWeightKg() {
        return prefs.getFloat(KEY_WEIGHT_KG, 0f);
    }

    public void setWeightKg(float weightKg) {
        prefs.edit().putFloat(KEY_WEIGHT_KG, weightKg).apply();
    }

    
    public int getHeightCm() {
        return prefs.getInt(KEY_HEIGHT_CM, 0);
    }

    public void setHeightCm(int heightCm) {
        prefs.edit().putInt(KEY_HEIGHT_CM, heightCm).apply();
    }

    
    public int getAge() {
        return prefs.getInt(KEY_AGE, 0);
    }

    public void setAge(int age) {
        prefs.edit().putInt(KEY_AGE, age).apply();
    }

    
    public String getGender() {
        return prefs.getString(KEY_GENDER, "");
    }

    public void setGender(String gender) {
        prefs.edit().putString(KEY_GENDER, gender).apply();
    }

    
    public String getPhotoPath() {
        return prefs.getString(KEY_PHOTO_PATH, "");
    }

    public void setPhotoPath(String path) {
        prefs.edit().putString(KEY_PHOTO_PATH, path).apply();
    }
}