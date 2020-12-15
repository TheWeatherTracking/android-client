package ru.ifmo.se.theweathertracking.android.ui.threeDays;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        threeDaysViewModel =
                new ViewModelProvider(this).get(ThreeDaysViewModel.class);
        View root = inflater.inflate(R.layout.fragment_three_days, container, false);
        final TextView textView = root.findViewById(R.id.text_three_days);

        threeDaysViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });

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

    @Override
    protected void onGetDataSuccess()
    {
        //this method is called when all data were received from sever and saved in telemetryDataSetViewModel
        if (telemetryDataSetViewModel.isEmpty()) {
            graphButton.setEnabled(false);
        } else {
            graphButton.setEnabled(true);
            Pair<ArrayList<String>, ArrayList<Integer>> temp = telemetryDataSetViewModel.getTemperatures("dd/MM HH:mm");
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