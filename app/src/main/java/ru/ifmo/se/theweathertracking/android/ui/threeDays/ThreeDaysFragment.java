package ru.ifmo.se.theweathertracking.android.ui.threeDays;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
    private ThreeDaysViewModel threeDaysViewModel;
    private Button graphButton;
    private View root;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        threeDaysViewModel =
                new ViewModelProvider(this).get(ThreeDaysViewModel.class);
        root = inflater.inflate(R.layout.fragment_three_days, container, false);

        graphButton = root.findViewById(R.id.btn_graph);
        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("type", FragmentType.THREE_DAYS.name());
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_three_days_to_graphFragmnet, bundle);
            }
        });

        telemetriesController = new TelemetriesController(getContext());
        telemetryDataSetViewModel = ((MainActivity)getActivity()).telemetryViewModel.ThreeDaysViewModel;

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

            TableLayout tableLayout = root.findViewById(R.id.table_three_days);

            for (int i = 0; i < telemetryDataSetViewModel.getTemperatures("dd/MM HH:mm").first.size(); i++) {
                TextView temperatureValueTextView = new TextView(getContext());
                temperatureValueTextView.setText(telemetryDataSetViewModel
                        .getTemperatures("dd/MM HH:mm").second.get(i).toString() + "°C");
                temperatureValueTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                TextView pressureValueTextView = new TextView(getContext());
                pressureValueTextView.setText(telemetryDataSetViewModel
                        .getPressures("dd/MM HH:mm").second.get(i).toString() + " mm Hg");
                pressureValueTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                TextView moistureValueTextView = new TextView(getContext());
                moistureValueTextView.setText(telemetryDataSetViewModel
                        .getMoisture("dd/MM HH:mm").second.get(i).toString() + " %");
                moistureValueTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                TextView luminosityValueTextView = new TextView(getContext());
                luminosityValueTextView.setText(telemetryDataSetViewModel
                        .getLuminosities("dd/MM HH:mm").second.get(i).toString() + " lx");
                luminosityValueTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                TextView tmstampValueTextView = new TextView(getContext());
                tmstampValueTextView.setText(telemetryDataSetViewModel
                        .getTemperatures("dd/MM HH:mm").first.get(i));
                tmstampValueTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                TableRow tableRow = new TableRow(getContext());

                tableRow.addView(tmstampValueTextView);
                tableRow.addView(temperatureValueTextView);
                tableRow.addView(pressureValueTextView);
                tableRow.addView(moistureValueTextView);
                tableRow.addView(luminosityValueTextView);

                tableLayout.addView(tableRow);
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