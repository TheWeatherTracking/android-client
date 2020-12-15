package ru.ifmo.se.theweathertracking.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import ru.ifmo.se.theweathertracking.api.model.TelemetryViewModel;
import ru.ifmo.se.theweathertracking.util.PropertiesManager;

public class MainActivity extends BaseActivity {

    private final String tag = "Main activity";
    private PropertiesManager propertiesManager;
    private AppBarConfiguration mAppBarConfiguration;
    public TelemetryViewModel telemetryViewModel;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_now, R.id.nav_today, R.id.nav_yesterday, R.id.nav_three_days, R.id.nav_devices)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        telemetryViewModel = new TelemetryViewModel();
        propertiesManager = new PropertiesManager(getApplicationContext());
        if (!propertiesManager.hasValidToken()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        if (!propertiesManager.hasDevice()) {
            //TODO: navigate to devices
        }

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.nav_header_main, navigationView, true);
        TextView emailText = view.findViewById(R.id.emailTextView);
        emailText.setText(propertiesManager.getLogin());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}