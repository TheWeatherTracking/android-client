package ru.ifmo.se.theweathertracking.api.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TelemetryModel {
    private final String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public Date Timestamp;
    public Integer Temperature;
    public Integer Pressure;
    public Integer Moisture;
    public Integer Luminosity;

    public TelemetryModel() {}
    public TelemetryModel(JSONObject jsonObject)
    {
        if (jsonObject.has("tmstamp")) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(dateFormat);
                this.Timestamp = format.parse(jsonObject.getString("tmstamp"));
                this.Temperature = getIntValue(jsonObject, "temperature") / 10;
                this.Pressure = getIntValue(jsonObject, "pressure");
                this.Moisture = getIntValue(jsonObject, "moisture");
                this.Luminosity = getIntValue(jsonObject, "luminosity");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static TelemetryModel[] getTelemetryArray(JSONArray jsonArray){
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

    private Integer getIntValue(JSONObject jsonObject, String fieldName){
        Integer value = null;

        if (jsonObject.has(fieldName)) {
            try {
                value = jsonObject.getInt(fieldName);
            } catch (JSONException ignored) {}
        }

        return value;
    }
}
