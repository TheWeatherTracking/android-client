package ru.ifmo.se.theweathertracking.android.ui.now;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.common.ANRequest;

import org.json.JSONObject;

import ru.ifmo.se.theweathertracking.android.DataFragment;
import ru.ifmo.se.theweathertracking.android.R;
import ru.ifmo.se.theweathertracking.api.TelemetriesController;
import ru.ifmo.se.theweathertracking.api.model.TelemetryModel;

public class NowFragment extends DataFragment {
    private TelemetriesController telemetriesController;
    private TelemetryModel telemetryModel;
    private NowViewModel nowViewModel;
    private View root;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        nowViewModel =
                new ViewModelProvider(this).get(NowViewModel.class);
        root = inflater.inflate(R.layout.fragment_now, container, false);

        telemetriesController = new TelemetriesController(getContext());

        //requests to get data from server
        loadData();

        return root;
    }

    @Override
    protected void parseResponse(JSONObject response) {
        //parse response
        telemetryModel = new TelemetryModel(response);
    }

    @Override
    protected ANRequest getRequest() {
        return telemetriesController.getCurrentTelemetry();
    }

    @Override
    protected void unauthorizedResponse() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_nav_now_to_loginActivity);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onGetDataSuccess() {
        //this method is called when all data were received from sever and saved in telemetryModel
        TextView temperatureTextView = root.findViewById(R.id.temperature_now);
        temperatureTextView.setText(telemetryModel.Temperature.toString() + "°C");

        TextView pressureValueTextView = root.findViewById(R.id.pressure_value_now);
        pressureValueTextView.setText(telemetryModel.Pressure.toString() + " mm Hg");

        TextView moistureValueTextView = root.findViewById(R.id.moisture_value_now);
        moistureValueTextView.setText(telemetryModel.Moisture.toString() + " %");

        TextView luminosityValueTextView = root.findViewById(R.id.luminosity_value_now);
        luminosityValueTextView.setText(telemetryModel.Luminosity.toString() + " lx");
    }

    @Override
    protected void onGetDataFailed(String message) {
        //this method is called when error response received from server
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}