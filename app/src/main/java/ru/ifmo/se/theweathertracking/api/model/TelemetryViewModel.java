package ru.ifmo.se.theweathertracking.api.model;

import ru.ifmo.se.theweathertracking.android.ui.FragmentType;

public class TelemetryViewModel {
    public TelemetryDataSetViewModel TodayViewModel;
    public TelemetryDataSetViewModel YesterdayViewModel;
    public TelemetryDataSetViewModel ThreeDaysViewModel;

    public TelemetryViewModel()
    {
        TodayViewModel = new TelemetryDataSetViewModel(FragmentType.TODAY);
        YesterdayViewModel = new TelemetryDataSetViewModel(FragmentType.YESTERDAY);
        ThreeDaysViewModel = new TelemetryDataSetViewModel(FragmentType.THREE_DAYS);
    }
}
