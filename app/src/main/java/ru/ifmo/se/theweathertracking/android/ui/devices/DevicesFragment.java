package ru.ifmo.se.theweathertracking.android.ui.devices;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.common.ANRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ru.ifmo.se.theweathertracking.android.DataFragment;
import ru.ifmo.se.theweathertracking.android.R;
import ru.ifmo.se.theweathertracking.api.DevicesController;
import ru.ifmo.se.theweathertracking.api.model.DeviceModel;
import ru.ifmo.se.theweathertracking.util.PropertiesManager;

public class DevicesFragment extends DataFragment {
    private DevicesController devicesController;
    private PropertiesManager propertiesManager;
    private ArrayList<DeviceModel> deviceModels;
    private View root;

    @SuppressLint("NonConstantResourceId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_devices, container, false);

        propertiesManager = new PropertiesManager(getContext());
        devicesController = new DevicesController(getContext());
        deviceModels = new ArrayList<>();

        //requests to get data from server
        loadData();

        return root;
    }

    //example of saving device value
    private void SaveChosenDevice(String deviceName){
        propertiesManager.saveDevice(deviceName);
    }

    @Override
    protected void parseResponse(JSONObject response) {
        //parse response
        try {
            JSONArray jsonArray = response
                    .getJSONObject("_embedded")
                    .getJSONArray("devices");
            deviceModels.clear();
            int length = jsonArray.length();
            for (int i=0; i<length; i++) {
                try {
                    deviceModels.add(new DeviceModel(jsonArray.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onGetDataFailed("Error occurred");
        }
    }

    @Override
    protected ANRequest getRequest() {
        return devicesController.getDevices();
    }

    @Override
    protected void unauthorizedResponse() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_nav_devices_to_loginActivity);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onGetDataSuccess() {
        //this method is called when all data were received from sever and saved in deviceModels
        RadioGroup radioGroup = root.findViewById(R.id.radio_group);

        Map<Integer, String> devices = new HashMap<Integer, String>();

        for (int i = 0; i < deviceModels.size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setId(i);
            radioButton.setText(deviceModels.get(i).Name);

            devices.put(radioButton.getId(), deviceModels.get(i).Name);

            radioGroup.addView(radioButton);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            SaveChosenDevice(devices.get(checkedId));

        });

    }

    @Override
    protected void onGetDataFailed(String message) {
        //this method is called when error response received from server
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}