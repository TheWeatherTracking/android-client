package ru.ifmo.se.theweathertracking.api;

import android.content.Context;

import com.androidnetworking.common.ANRequest;

public class DevicesController extends BaseController {
    public DevicesController(Context ctx) {
        super(ctx);
    }

    public ANRequest getDevices() {
        String path = "/devices";
        return getGetRequestWithAuth(path)
                .build();
    }
}
