package ru.ifmo.se.theweathertracking.android;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import ru.ifmo.se.theweathertracking.util.PropertiesManager;

public class MainActivity extends BaseActivity {
    private final String tag = "Main activity";
    private PropertiesManager propertiesManager;

    @Override
    protected String getTag() {
        return this.tag;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.app_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        propertiesManager = new PropertiesManager(getApplicationContext());

        if (!propertiesManager.hasValidToken()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}