package ru.ifmo.se.theweathertracking.api;

import android.content.Context;

import com.androidnetworking.common.ANRequest;

import org.json.JSONException;
import org.json.JSONObject;

// api/users
public class UsersController extends BaseController {

    public UsersController(Context ctx) {
        super(ctx);
    }

    // POST api/users
    public ANRequest getLoginRequest(String userName, String password) {
//TODO: implement right login request
        return getPostRequest("/users")
                .addJSONObjectBody(createLoginRequest(userName, password))
                .build();
    }

    // POST api/users
    public ANRequest getSignUpRequest(String userName, String password) {
//TODO: implement right sign up request
        return getPostRequest("/users")
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