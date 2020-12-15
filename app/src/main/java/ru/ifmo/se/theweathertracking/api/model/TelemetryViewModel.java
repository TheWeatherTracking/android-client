package ru.ifmo.se.theweathertracking.api.model;

import ru.ifmo.se.theweathertracking.android.ui.graph.GraphType;
import ru.ifmo.se.theweathertracking.android.ui.graph.GraphViewModel;

public class TelemetryViewModel {
    public GraphViewModel TodayViewModel;
    public GraphViewModel YesterdayViewModel;
    public GraphViewModel ThreeDaysViewModel;

    public TelemetryViewModel()
    {
        TodayViewModel = new GraphViewModel(GraphType.TODAY);
        YesterdayViewModel = new GraphViewModel(GraphType.YESTERDAY);
        ThreeDaysViewModel = new GraphViewModel(GraphType.THREE_DAYS);
    }
}
