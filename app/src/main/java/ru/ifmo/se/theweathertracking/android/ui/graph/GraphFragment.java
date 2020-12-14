package ru.ifmo.se.theweathertracking.android.ui.graph;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.charts.BarChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.ifmo.se.theweathertracking.android.R;
import ru.ifmo.se.theweathertracking.api.TelemetriesController;
import ru.ifmo.se.theweathertracking.api.model.TelemetryModel;

public class GraphFragment extends Fragment {
    private TelemetriesController telemetriesController;
    private GraphBuilder graphBuilder;
    private GraphType graphType;
    private String dateFormat;

    BarChart temperatureChart;
    BarChart pressureChart;
    BarChart moistureChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_graph, container, false);
        telemetriesController =  new TelemetriesController(getContext());
        graphBuilder = new GraphBuilder();

        graphType = GraphType.valueOf(getArguments().getString("type", "TODAY"));
        temperatureChart = (BarChart) root.findViewById(R.id.temp_chart);
        pressureChart = (BarChart) root.findViewById(R.id.pres_chart);
        moistureChart = (BarChart) root.findViewById(R.id.moist_chart);

        getDataSet();

        return root;
    }

    private void getDataSet() {
        ANRequest request;

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        switch (graphType){
            case YESTERDAY:
                request = telemetriesController.getYesterdayTelemetry();
                dateFormat = "HH:mm";
                break;
            case THREE_DAYS:
                request = telemetriesController.getThreeDaysTelemetry();
                dateFormat = "dd.MM";
                break;
            case TODAY:
            default:
                request = telemetriesController.getTodayTelemetry();
                dateFormat = "HH:mm";
        }

        request.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response
                            .getJSONObject("_embedded")
                            .getJSONArray("telemetries");
                    if (response.length() == 0) {
                        OnGetDataFailed("No data available");
                    }
                    TelemetryModel[] array = getTelemetryArray(jsonArray);
                    GraphViewModel viewModel = new GraphViewModel(array);
                    OnGetDataSuccess(viewModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                    OnGetDataFailed("Error occurred");
                } finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onError(ANError anError) {
                OnGetDataFailed("Error occurred");
                progressDialog.dismiss();
            }
        });

    }

    public void OnGetDataSuccess(GraphViewModel viewModel) {
        // draw graphs
        Pair<ArrayList<String>, ArrayList<Integer>> temp = viewModel.getTemperatures(dateFormat);
        graphBuilder.fillBarChart(temperatureChart, "Temperature", temp.first, temp.second);

        Pair<ArrayList<String>, ArrayList<Integer>> pres = viewModel.getPressures(dateFormat);
        graphBuilder.fillBarChart(pressureChart, "Pressure", pres.first, pres.second);

        Pair<ArrayList<String>, ArrayList<Integer>> moist = viewModel.getMoisture(dateFormat);
        graphBuilder.fillBarChart(moistureChart, "Moisture", moist.first, moist.second);
    }

    public void OnGetDataFailed(String message) {
        // TODO: handle error, check unauthorized response
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private TelemetryModel[] getTelemetryArray(JSONArray jsonArray){
        int length = jsonArray.length();
        TelemetryModel[] array = new TelemetryModel[length];
        for (int i=0; i<length; i++) {
            try {
                array[i] = new TelemetryModel(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return array;
    }
}