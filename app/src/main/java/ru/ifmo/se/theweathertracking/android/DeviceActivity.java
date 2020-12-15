package ru.ifmo.se.theweathertracking.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;
import ru.ifmo.se.theweathertracking.api.DevicesController;
import ru.ifmo.se.theweathertracking.api.model.DeviceModel;
import ru.ifmo.se.theweathertracking.util.PropertiesManager;

public class DeviceActivity extends BaseActivity {
    private DevicesController devicesController;
    private PropertiesManager propertiesManager;
    private ArrayList<DeviceModel> deviceModels;

    @Override
    protected String getTag() {
        return "Device activity";
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_device;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.app_name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        propertiesManager = new PropertiesManager(ctx);
        devicesController = new DevicesController(ctx);
        deviceModels = new ArrayList<>();

        final ProgressDialog progressDialog = new ProgressDialog(DeviceActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        devicesController.getDevices()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response
                                    .getJSONObject("_embedded")
                                    .getJSONArray("devices");
                            setDeviceModels(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        onGetDevicesSuccess();
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        if (error.getErrorCode() == 403) {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            onGetDevicesFailed("Error occurred");
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    public void onGetDevicesSuccess() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.devicesLayout);

        int i = 1;
        for (DeviceModel device : deviceModels) {
            Button btnTag = new Button(this);
            btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            btnTag.setText(device.Name);
            btnTag.setId(i++);
            btnTag.setOnClickListener((View v) -> {
                propertiesManager.saveDevice(device.Name);
                finish();
            });
            layout.addView(btnTag);
        }
    }


    public void onGetDevicesFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    private void setDeviceModels(JSONArray jsonArray) {
        deviceModels.clear();
        int length = jsonArray.length();
        for (int i=0; i<length; i++) {
            try {
                deviceModels.add(new DeviceModel(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
