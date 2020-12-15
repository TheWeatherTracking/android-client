package ru.ifmo.se.theweathertracking.android.ui.today;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ru.ifmo.se.theweathertracking.android.R;

public class TodayFragment extends TelemetryFragment {
    private TelemetriesController telemetriesController;

    private TodayViewModel todayViewModel;
    private Button graphButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        todayViewModel =
                new ViewModelProvider(this).get(TodayViewModel.class);
        View root = inflater.inflate(R.layout.fragment_today, container, false);
        final TextView textView = root.findViewById(R.id.text_today);
        todayViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        telemetriesController = new TelemetriesController(getContext());
        telemetryDataSetViewModel = ((MainActivity)getActivity()).telemetryViewModel.TodayViewModel;
        graphButton = root.findViewById(R.id.btn_graph);

        //requests to get data from server
        loadTelemetryData();
        graphButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putIntArray("values", new int[] {50, 120, 90});
            Navigation.findNavController(view)
                    .navigate(R.id.action_nav_today_to_graphFragmnet, bundle);
        });

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
