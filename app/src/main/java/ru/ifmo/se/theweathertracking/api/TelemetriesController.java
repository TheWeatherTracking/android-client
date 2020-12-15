package ru.ifmo.se.theweathertracking.api;

import android.content.Context;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

// api/telemetries
public class TelemetriesController extends BaseController {
    private final SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
    private final long dayInMillis = 24*60*60*1000;

    public TelemetriesController(Context ctx) { super(ctx); }

    public ANRequest getTodayTelemetry(int size, int page) {
        String device = propertiesManager.getDevice();
        String date = dateFormater.format(new Date(System.currentTimeMillis() - dayInMillis));
        String format = "telemetries/search/getAfterByDevice?page=%d&size=%d&sort=tmstamp&timestamp=%s&device_signature=%s";
        String path = String.format(format, page, size, date, device);

        return getGetRequestWithAuth(path)
                .build();
    }

    public ANRequest getYesterdayTelemetry(int size, int page) {
        String device = propertiesManager.getDevice();
        Date date = new Date(System.currentTimeMillis() - 2*dayInMillis);
        String format = "telemetries/search/getAfterByDevice?page=%d&size=%d&sort=tmstamp&timestamp=%s&device_signature=%s";
        String path = String.format(format, page, size, dateFormater.format(date), device);

        return getGetRequestWithAuth(path)
                .build();
    }

    public ANRequest getThreeDaysTelemetry(int size, int page) {
        String device = propertiesManager.getDevice();
        Date date = new Date(System.currentTimeMillis() - 3*dayInMillis);
        String format = "telemetries/search/getAfterByDevice?page=%d&size=%d&sort=tmstamp&timestamp=%s&device_signature=%s";
        String path = String.format(format, page, size, dateFormater.format(date), device);

        return getGetRequestWithAuth(path)
                .build();
    }

    public ANRequest getCurrentTelemetry() {
        String device = propertiesManager.getDevice();
        String format = "telemetries/search/getCurrentByDevice?device_signature=%s";
        String path = String.format(format, device);
        return getGetRequestWithAuth(path)
                .build();
    }
}
