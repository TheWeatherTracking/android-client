package ru.ifmo.se.theweathertracking.android.ui.threeDays;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.common.ANRequest;

import java.util.ArrayList;

import ru.ifmo.se.theweathertracking.android.MainActivity;
import ru.ifmo.se.theweathertracking.android.R;
import ru.ifmo.se.theweathertracking.android.TelemetryFragment;
import ru.ifmo.se.theweathertracking.android.ui.FragmentType;
import ru.ifmo.se.theweathertracking.api.TelemetriesController;

public class ThreeDaysFragment extends TelemetryFragment {
    private TelemetriesController telemetriesController;
    private Button graphButton;
    private View root;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_three_days, container, false);

        graphButton = root.findViewById(R.id.btn_graph);
        graphButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("type", FragmentType.THREE_DAYS.name());
            Navigation.findNavController(view)
                    .navigate(R.id.action_nav_three_days_to_graphFragmnet, bundle);
        });

        telemetriesController = new TelemetriesController(getContext());
        telemetryDataSetViewModel = ((MainActivity) requireActivity()).telemetryViewModel.ThreeDaysViewModel;

        //requests to get data from server
        loadTelemetryData();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onGetDataSuccess()
    {
        //this method is called when all data were received from sever and saved in telemetryDataSetViewModel
        if (telemetryDataSetViewModel.isEmpty()) {
            graphButton.setEnabled(false);
            Toast.makeText(getContext(), "Data is empty :(", Toast.LENGTH_LONG).show();
        } else {
            graphButton.setEnabled(true);
            Pair<ArrayList<String>, ArrayList<Integer>> temp = telemetryDataSetViewModel.getTemperatures("dd/MM HH:mm");

            TableLayout table = root.findViewById(R.id.table_three_days);
            table.setStretchAllColumns(true);
            table.setShrinkAllColumns(true);

            for (int i = 0; i < telemetryDataSetViewModel.getTemperatures("dd/MM HH:mm").first.size(); i++) {
                TextView temperatureValueTextView = new TextView(getContext());

                temperatureValueTextView.setText( telemetryDataSetViewModel
                        .getTemperatures("dd/MM HH:mm").second.get(i).toString() + "°C");
                temperatureValueTextView.setGravity(Gravity.CENTER);

                TextView pressureValueTextView = new TextView(getContext());
                pressureValueTextView.setText(telemetryDataSetViewModel
                        .getPressures("dd/MM HH:mm").second.get(i).toString() + " mm Hg");
                pressureValueTextView.setGravity(Gravity.CENTER);

                TextView moistureValueTextView = new TextView(getContext());
                moistureValueTextView.setText(telemetryDataSetViewModel
                        .getMoisture("dd/MM HH:mm").second.get(i).toString() + " %");
                moistureValueTextView.setGravity(Gravity.CENTER);

                TextView luminosityValueTextView = new TextView(getContext());
                luminosityValueTextView.setText(telemetryDataSetViewModel
                        .getLuminosities("dd/MM HH:mm").second.get(i).toString() + " lx");
                luminosityValueTextView.setGravity(Gravity.CENTER);

                TextView tmstampValueTextView = new TextView(getContext());
                tmstampValueTextView.setText(telemetryDataSetViewModel
                        .getTemperatures("dd/MM HH:mm:ss").first.get(i));
                tmstampValueTextView.setGravity(Gravity.CENTER);

                TableRow tableRow = new TableRow(getContext());
                TableRow.LayoutParams params = new TableRow.LayoutParams();
                params.span = 6;
                params.topMargin = 20;

                tableRow.addView(tmstampValueTextView, params);
                tableRow.addView(temperatureValueTextView, params);
                tableRow.addView(pressureValueTextView, params);
                tableRow.addView(moistureValueTextView, params);
                tableRow.addView(luminosityValueTextView, params);

                table.addView(tableRow);
        }

        }
    }

    @Override
    public void onGetDataFailed(String message) {
        //this method is called when error response received from server
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void unauthorizedResponse() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_nav_three_days_to_loginActivity);
    }

    @Override
    protected ANRequest getRequest(int size, int page) {
        return telemetriesController.getThreeDaysTelemetry(size, page);
    }
}