package com.example.runtracker;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    private static final String PREFS_NAME = "sprint_user_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_JOIN_YEAR = "join_year";
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
}