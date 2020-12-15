package ru.ifmo.se.theweathertracking.api.model;

import android.util.Pair;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.ifmo.se.theweathertracking.android.ui.FragmentType;

public class TelemetryDataSetViewModel {
    private final long minuteInMillis = 60000;
    private Map<Date, Integer> temperatures;
    private Map<Date, Integer> pressures;
    private Map<Date, Integer> moisture;
    private Map<Date, Integer> luminosities;
    private FragmentType fragmentType;

    public TelemetryDataSetViewModel(FragmentType type) {
        this.fragmentType = type;
        this.temperatures = new HashMap<>();
        this.pressures = new HashMap<>();
        this.moisture = new HashMap<>();
        this.luminosities = new HashMap<>();
    }

    public TelemetryDataSetViewModel(TelemetryModel[] telemetries, FragmentType type) {
        this.fragmentType = type;
        this.temperatures = new HashMap<>();
        this.pressures = new HashMap<>();
        this.moisture = new HashMap<>();
        this.luminosities = new HashMap<>();
        addTelemetries(telemetries);
    }

    public boolean isEmpty(){
        return this.temperatures.size() == 0
                && this.pressures.size() == 0
                && this.moisture.size() == 0
                && this.luminosities.size() == 0;
    }

    public void addTelemetries(TelemetryModel[] telemetries) {
        if (telemetries.length == 0) return;
        Date currentDate = new Date(System.currentTimeMillis());
        currentDate.setHours(0); currentDate.setMinutes(0); currentDate.setSeconds(0);

        int toDate = currentDate.getDate();
        if (fragmentType != FragmentType.YESTERDAY) toDate++;

        int fromDate = currentDate.getDate();
        int dateDiff = fragmentType == FragmentType.THREE_DAYS ? 3 : (fragmentType == FragmentType.YESTERDAY ? 2 : 1);
        fromDate -= dateDiff;
        long minTimeDiff = -2*minuteInMillis + (fragmentType == FragmentType.THREE_DAYS
                ? 3*60*minuteInMillis
                : 60*minuteInMillis);

        Date previousDate = null;
        for (TelemetryModel telemetry : telemetries) {
            long diff = previousDate == null ? 0 : Math.abs(telemetry.Timestamp.getTime() - previousDate.getTime());
            if (telemetry.Timestamp != null &&
                    (telemetry.Timestamp.getDate() > fromDate && telemetry.Timestamp.getDate() < toDate) &&
                    (previousDate == null || diff >= minTimeDiff)) {
                previousDate = new Date(telemetry.Timestamp.getTime() + minTimeDiff);
                if (telemetry.Temperature != null)
                    this.temperatures.put(telemetry.Timestamp, telemetry.Temperature);
                if (telemetry.Pressure != null)
                    this.pressures.put(telemetry.Timestamp, telemetry.Pressure);
                if (telemetry.Moisture != null)
                    this.moisture.put(telemetry.Timestamp, telemetry.Moisture);
                if (telemetry.Luminosity != null)
                    this.luminosities.put(telemetry.Timestamp, telemetry.Luminosity);
            }
        }
    }

    public Pair<ArrayList<String>, ArrayList<Integer>> getTemperatures(String dateFormat) {
        return mapToArrayListPair(this.temperatures, dateFormat);
    }

    public Pair<ArrayList<String>, ArrayList<Integer>> getPressures(String dateFormat) {
        return mapToArrayListPair(this.pressures, dateFormat);
    }

    public Pair<ArrayList<String>, ArrayList<Integer>> getMoisture(String dateFormat) {
        return mapToArrayListPair(this.moisture, dateFormat);
    }

    public Pair<ArrayList<String>, ArrayList<Integer>> getLuminosities(String dateFormat) {
        return mapToArrayListPair(this.luminosities, dateFormat);
    }


    private Pair<ArrayList<String>, ArrayList<Integer>> mapToArrayListPair(Map<Date, Integer> map, String dateFormat) {
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        DateFormat format = new SimpleDateFormat(dateFormat);
        ArrayList<Date> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        for (Date key: keys) {
            if (map.get(key) != null) {
                labels.add(format.format(key));
                values.add(map.get(key));
            }
        }

        return new Pair<>(labels, values);
    }
}
