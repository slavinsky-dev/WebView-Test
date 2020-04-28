package com.slavinskydev.trafficdevilstest.screens.loader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPreferencesManager {
    private  static final String APP_PREFERENCES_FIRST_RUN = "firstrun";
    private  static final String APP_PREFERENCES_NEXT_RUN = "nextrun";

    private static boolean firstRun;

    private static SharedPreferences sharedPreferencesFirstRun;
    private static SharedPreferences sharedPreferencesNextRun;
    @SuppressLint("StaticFieldLeak")
    private static SharedPreferencesManager instance;
    Context context;

    private SharedPreferencesManager(Context context){
        sharedPreferencesFirstRun = context.getApplicationContext().getSharedPreferences(APP_PREFERENCES_FIRST_RUN, Context.MODE_PRIVATE);
        sharedPreferencesNextRun = context.getApplicationContext().getSharedPreferences(APP_PREFERENCES_NEXT_RUN, Context.MODE_PRIVATE);
    }

    static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null)
            instance = new SharedPreferencesManager(context);
        return instance;
    }

    static boolean loadFirstRunPreferences() {
        return sharedPreferencesFirstRun.getBoolean(APP_PREFERENCES_FIRST_RUN, true);
    }

    static void setFirstRunPreferences() {
        firstRun = sharedPreferencesFirstRun.edit().putBoolean(APP_PREFERENCES_FIRST_RUN, false).commit();
    }

    public static void activateFirstRun() {
        firstRun = sharedPreferencesFirstRun.edit().putBoolean(APP_PREFERENCES_FIRST_RUN, true).commit();
    }

    static void setNextRunPreferences(boolean state) {
        boolean nextRun = sharedPreferencesNextRun.edit().putBoolean(APP_PREFERENCES_NEXT_RUN, state).commit();
    }

    static boolean loadNextRunPreferences() {
        return sharedPreferencesNextRun.getBoolean(APP_PREFERENCES_NEXT_RUN, false);
    }

}
