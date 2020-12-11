package ru.ifmo.se.theweathertracking.android;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract String getTag();

    protected abstract int getLayout();

    protected abstract int getActivityTitle();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getLayout());
        Log.v(this.getTag(), "[ ON CREATE ]");
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.v(this.getTag(), "[ ON POST CREATE ]");
    }

    protected void onRestart() {
        super.onRestart();
        Log.v(this.getTag(), "[ ON RESTART ]");
    }

    protected void onStart() {
        super.onStart();
        Log.v(this.getTag(), "[ ON START ]");
    }

    protected void onResume() {
        super.onResume();
        Log.v(this.getTag(), "[ ON RESUME ]");
    }

    protected void onPostResume() {
        super.onPostResume();
        Log.v(this.getTag(), "[ ON STOP ]");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.v(this.getTag(), "[ ON DESTROY ]");
    }
}