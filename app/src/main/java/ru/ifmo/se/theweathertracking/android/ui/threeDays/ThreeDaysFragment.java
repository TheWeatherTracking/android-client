package ru.ifmo.se.theweathertracking.android.ui.threeDays;

import android.app.ProgressDialog;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.ifmo.se.theweathertracking.android.MainActivity;
import ru.ifmo.se.theweathertracking.android.R;
import ru.ifmo.se.theweathertracking.android.ui.graph.GraphType;
import ru.ifmo.se.theweathertracking.android.ui.graph.GraphViewModel;
import ru.ifmo.se.theweathertracking.api.TelemetriesController;
import ru.ifmo.se.theweathertracking.api.model.TelemetryModel;

public class ThreeDaysFragment extends Fragment {
    private TelemetriesController telemetriesController;
    private ThreeDaysViewModel threeDaysViewModel;
    private GraphViewModel graphViewModel;
    private Button graphButton;
    private int pageLoaded;
    private int pageCount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        threeDaysViewModel =
                new ViewModelProvider(this).get(ThreeDaysViewModel.class);
        View root = inflater.inflate(R.layout.fragment_three_days, container, false);
        final TextView textView = root.findViewById(R.id.text_three_days);
        graphButton = root.findViewById(R.id.btn_graph);

        threeDaysViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("type", GraphType.THREE_DAYS.name());
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_three_days_to_graphFragmnet, bundle);
            }
        });

        telemetriesController = new TelemetriesController(getContext());
        graphViewModel = ((MainActivity)getActivity()).telemetryViewModel.ThreeDaysViewModel;

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        pageLoaded = 0;
        sendRequest(progressDialog, -1, 1);

        return root;
    }

    private void sendRequest(ProgressDialog progressDialog, int size, int page) {
        if (size > 0 && 1000*page < size)
        {
            sendRequest(progressDialog, size, page+1);
        }

        ANRequest request = telemetriesController.getThreeDaysTelemetry(Math.min(size, 1000), page-1);
        request.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (size == -1)
                    {
                        int elementsCount = response
                                .getJSONObject("page")
                                .getInt("totalElements");
                        sendRequest(progressDialog, elementsCount, page);
                    } else {
                        JSONArray jsonArray = response
                                .getJSONObject("_embedded")
                                .getJSONArray("telemetries");
                        pageCount = response
                                .getJSONObject("page")
                                .getInt("totalPages");
                        if (response.length() == 0) {
                            OnGetDataFailed("No data available");
                        }
                        TelemetryModel[] array = TelemetryModel.getTelemetryArray(jsonArray);
                        graphViewModel.addTelemetries(array);
                        pageLoaded++;
                        if (pageLoaded >= pageCount)
                        {
                            onGetDataSuccess();
                            progressDialog.dismiss();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    OnGetDataFailed("Error occurred");
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onError(ANError anError) {
                if (anError.getErrorCode() == 403) {
                    unauthorizedRequest();
                } else {
                    OnGetDataFailed("Error occurred");
                }
                progressDialog.dismiss();
            }
        });
    }

    public void onGetDataSuccess()
    {
        //display info from telemetry model
        if (graphViewModel.isEmpty()) {
            graphButton.setEnabled(false);
        } else {
            graphButton.setEnabled(true);
            LinearLayout layout = (LinearLayout) getView().findViewById(R.id.three_days_layout);
            Pair<ArrayList<String>, ArrayList<Integer>> temp = graphViewModel.getTemperatures("dd/MM HH:mm");

            for (int i=0; i<temp.first.size(); i++){
                TextView textView = new TextView(getContext());
                textView.setText(temp.first.get(i) + " " + temp.second.get(i));
                layout.addView(textView);
            }
        }
    }

    private void unauthorizedRequest() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_nav_three_days_to_loginActivity);
    }

    public void OnGetDataFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}