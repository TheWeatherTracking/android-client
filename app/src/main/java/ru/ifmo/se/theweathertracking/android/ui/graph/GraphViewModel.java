package ru.ifmo.se.theweathertracking.android.ui.graph;

import android.util.Pair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.ifmo.se.theweathertracking.api.model.TelemetryModel;

public class GraphViewModel {
    private Map<Date, Integer> temperatures;
    private Map<Date, Integer> pressures;
    private Map<Date, Integer> moisture;
    private Map<Date, Integer> luminosities;

    public GraphViewModel() {
        this.temperatures = new HashMap<>();
        this.pressures = new HashMap<>();
        this.moisture = new HashMap<>();
        this.luminosities = new HashMap<>();
    }

    public GraphViewModel(TelemetryModel[] telemetries)
    {
        this.temperatures = new HashMap<>();
        this.pressures = new HashMap<>();
        this.moisture = new HashMap<>();
        this.luminosities = new HashMap<>();
        for (TelemetryModel telemetry : telemetries) {
            if (telemetry.Timestamp != null) {
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
