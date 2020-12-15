package ru.ifmo.se.theweathertracking.api.model;

import ru.ifmo.se.theweathertracking.android.ui.graph.GraphType;

public class TelemetryViewModel {
    public TelemetryDataSetViewModel TodayViewModel;
    public TelemetryDataSetViewModel YesterdayViewModel;
    public TelemetryDataSetViewModel ThreeDaysViewModel;

    public TelemetryViewModel()
    {
        TodayViewModel = new TelemetryDataSetViewModel(GraphType.TODAY);
        YesterdayViewModel = new TelemetryDataSetViewModel(GraphType.YESTERDAY);
        ThreeDaysViewModel = new TelemetryDataSetViewModel(GraphType.THREE_DAYS);
    }
}
