package ru.ifmo.se.theweathertracking.api.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DeviceModel {

    public String Name;

    public DeviceModel() {}
    public DeviceModel(JSONObject jsonObject)
    {
        if (jsonObject.has("signature")) {
            try {
                this.Name = jsonObject.getString("signature");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
