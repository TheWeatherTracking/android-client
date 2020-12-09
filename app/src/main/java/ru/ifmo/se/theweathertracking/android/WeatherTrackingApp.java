package ru.ifmo.se.theweathertracking.android;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.androidnetworking.AndroidNetworking;

public final class WeatherTrackingApp extends Application {
    private static final String tag = "weatherTrackingApp";
    private static Context ctx;

    public void onCreate() {
        super.onCreate();
        ctx = this.getApplicationContext();
        AndroidNetworking.initialize(ctx);
        Log.v(tag, "[ ON CREATE ]");
    }

    public void onLowMemory() {
        super.onLowMemory();
        Log.w(tag, "[ ON LOW MEMORY ]");
    }

    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.d(tag, "[ ON TRIM MEMORY ]: " + level);
    }
}