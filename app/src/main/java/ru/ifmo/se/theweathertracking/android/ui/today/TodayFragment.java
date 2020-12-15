package ru.ifmo.se.theweathertracking.android.ui.today;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import com.androidnetworking.common.ANRequest;
import java.util.ArrayList;

import ru.ifmo.se.theweathertracking.android.MainActivity;
import ru.ifmo.se.theweathertracking.android.R;
import ru.ifmo.se.theweathertracking.android.TelemetryFragment;
import ru.ifmo.se.theweathertracking.android.ui.graph.GraphType;
import ru.ifmo.se.theweathertracking.api.TelemetriesController;

public class TodayFragment extends TelemetryFragment {
    private TelemetriesController telemetriesController;

    Button graphButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_today, container, false);

        graphButton = root.findViewById(R.id.btn_graph);
        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("type", GraphType.TODAY.name());
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_today_to_graphFragmnet, bundle);
            }
        });

        telemetriesController = new TelemetriesController(getContext());
        telemetryDataSetViewModel = ((MainActivity)getActivity()).telemetryViewModel.TodayViewModel;

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
            LinearLayout layout = (LinearLayout) getView().findViewById(R.id.today_layout);
            Pair<ArrayList<String>, ArrayList<Integer>> temp = telemetryDataSetViewModel.getTemperatures("dd/MM HH:mm");

            for (int i=0; i<temp.first.size(); i++){
                TextView textView = new TextView(getContext());
                textView.setText(temp.first.get(i) + " " + temp.second.get(i));
                layout.addView(textView);
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
                .navigate(R.id.action_nav_today_to_loginActivity);
    }

    @Override
    protected ANRequest getRequest(int size, int page) {
        return telemetriesController.getTodayTelemetry(size, page);
    }
}
