package ru.ifmo.se.theweathertracking.api;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import ru.ifmo.se.theweathertracking.android.R;
import ru.ifmo.se.theweathertracking.util.PropertiesManager;

public class BaseController {

    protected Context context;
    private PropertiesManager propertiesManager;

    public BaseController(Context ctx) {

        context = ctx;
        propertiesManager = new PropertiesManager(ctx);
    }

    protected ANRequest.PostRequestBuilder getPostRequest(String resourcePath) {
        String requestPath = context.getString(R.string.api_root_url) +
                "/api/" +
                resourcePath;

        return AndroidNetworking.post(requestPath)
                .setContentType("application/json");

    }

    protected ANRequest.GetRequestBuilder getGetRequest(String resourcePath) {
        String requestPath = context.getString(R.string.api_root_url) +
                "/api/" +
                resourcePath;

        String apiToken = propertiesManager.getToken();

        return AndroidNetworking.get(requestPath)
                .addHeaders("Authorization", "Basic " + apiToken);

    }
}