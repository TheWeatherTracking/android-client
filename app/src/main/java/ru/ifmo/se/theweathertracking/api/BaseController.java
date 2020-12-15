package ru.ifmo.se.theweathertracking.api;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import ru.ifmo.se.theweathertracking.android.R;
import ru.ifmo.se.theweathertracking.util.PropertiesManager;

public class BaseController {

    protected Context context;
    protected PropertiesManager propertiesManager;

    public BaseController(Context ctx) {

        context = ctx;
        propertiesManager = new PropertiesManager(ctx);
    }

    protected ANRequest.PostRequestBuilder getPostRequestWithFormData(String resourcePath) {
        String requestPath = context.getString(R.string.api_root_url) +
                "/api/" +
                resourcePath;

        return AndroidNetworking.post(requestPath)
                .setContentType("application/x-www-form-urlencoded");
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

        return AndroidNetworking.get(requestPath);

    }

    protected ANRequest.PostRequestBuilder getPostRequestWithAuth(String resourcePath) {
        String apiToken = propertiesManager.getToken();

        return getPostRequest(resourcePath)
                .addHeaders("Authorization", "Basic " + apiToken);

    }

    protected ANRequest.GetRequestBuilder getGetRequestWithAuth(String resourcePath) {
        String apiToken = propertiesManager.getToken();

        return getGetRequest(resourcePath)
                .addHeaders("Authorization", "Basic " + apiToken);

    }
}