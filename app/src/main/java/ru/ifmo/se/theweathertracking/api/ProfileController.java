package ru.ifmo.se.theweathertracking.api;

import android.content.Context;

import com.androidnetworking.common.ANRequest;

import org.json.JSONException;
import org.json.JSONObject;

// example of profile api
public class ProfileController extends BaseController {

    public ProfileController(Context ctx) {
        super(ctx);
    }

    // POST api/profile/login
    public ANRequest getLoginRequest(String userName, String password) {

        return getPostRequest("/profile/login")
                .addJSONObjectBody(createLoginRequest(userName, password))
                .build();
    }

    // {"UserName": userName, "Password": password}
    private JSONObject createLoginRequest(String userName, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserName", userName);
            jsonObject.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
