package ru.ifmo.se.theweathertracking.api;

import android.content.Context;

import com.androidnetworking.common.ANRequest;

// api/telemetries
public class TelemetriesController extends BaseController {
    public TelemetriesController(Context ctx) { super(ctx); }

    // TODO: implement right requests to get different time data
    public ANRequest getTodayTelemetry() {
        return getGetRequestWithAuth("/telemetries?sort=tmstamp")
                .build();
    }

    public ANRequest getYesterdayTelemetry() {
        return getGetRequestWithAuth("/telemetries?sort=tmstamp")
                .build();
    }

    public ANRequest getThreeDaysTelemetry() {
        return getGetRequestWithAuth("/telemetries?sort=tmstamp")
                .build();
    }

    public ANRequest getCurrentTelemetry() {
        return getGetRequestWithAuth("/telemetries?sort=tmstamp")
                .build();
    }
}
