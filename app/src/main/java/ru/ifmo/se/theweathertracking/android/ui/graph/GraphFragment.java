package ru.ifmo.se.theweathertracking.android.ui.graph;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;

import ru.ifmo.se.theweathertracking.android.MainActivity;
import ru.ifmo.se.theweathertracking.android.R;
import ru.ifmo.se.theweathertracking.android.ui.FragmentType;
import ru.ifmo.se.theweathertracking.api.TelemetriesController;
import ru.ifmo.se.theweathertracking.api.model.TelemetryDataSetViewModel;

public class GraphFragment extends Fragment {
    private TelemetriesController telemetriesController;
    private GraphBuilder graphBuilder;
    private FragmentType fragmentType;
    private String dateFormat;
    private TelemetryDataSetViewModel viewModel;

    BarChart temperatureChart;
    BarChart pressureChart;
    BarChart moistureChart;
    BarChart luminosityChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_graph, container, false);
        telemetriesController =  new TelemetriesController(getContext());
        graphBuilder = new GraphBuilder();
        fragmentType = FragmentType.valueOf(getArguments().getString("type", "TODAY"));

        temperatureChart = (BarChart) root.findViewById(R.id.temp_chart);
        pressureChart = (BarChart) root.findViewById(R.id.pres_chart);
        moistureChart = (BarChart) root.findViewById(R.id.moist_chart);
        luminosityChart = (BarChart) root.findViewById(R.id.luminosity_chart);

        switch (fragmentType){
            case YESTERDAY:
                dateFormat = "HH:mm";
                viewModel = ((MainActivity)getActivity()).telemetryViewModel.YesterdayViewModel;
                break;
            case THREE_DAYS:
                dateFormat = "dd/MM";
                viewModel = ((MainActivity)getActivity()).telemetryViewModel.ThreeDaysViewModel;
                break;
            case TODAY:
            default:
                dateFormat = "HH:mm";
                viewModel = ((MainActivity)getActivity()).telemetryViewModel.TodayViewModel;
        }

        DrawGraphs(viewModel);

        return root;
    }

    public void DrawGraphs(TelemetryDataSetViewModel viewModel) {
        Pair<ArrayList<String>, ArrayList<Integer>> temp = viewModel.getTemperatures(dateFormat);
        graphBuilder.fillBarChart(temperatureChart, "Temperature", temp.first, temp.second, R.color.temperature_graph);

        Pair<ArrayList<String>, ArrayList<Integer>> pres = viewModel.getPressures(dateFormat);
        graphBuilder.fillBarChart(pressureChart, "Pressure", pres.first, pres.second, R.color.pressure_graph);

        Pair<ArrayList<String>, ArrayList<Integer>> moist = viewModel.getMoisture(dateFormat);
        graphBuilder.fillBarChart(moistureChart, "Moisture", moist.first, moist.second, R.color.moisture_graph);

        Pair<ArrayList<String>, ArrayList<Integer>> luminos = viewModel.getLuminosities(dateFormat);
        graphBuilder.fillBarChart(luminosityChart, "Luminosity", luminos.first, luminos.second, R.color.luminosity_graph);
    }
}