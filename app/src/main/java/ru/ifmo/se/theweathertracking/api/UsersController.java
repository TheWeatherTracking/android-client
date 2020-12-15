package ru.ifmo.se.theweathertracking.api;

import android.content.Context;

import com.androidnetworking.common.ANRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ru.ifmo.se.theweathertracking.util.PropertiesManager;

// api/users
public class UsersController extends BaseController {

    public UsersController(Context ctx) {
        super(ctx);
    }

    public ANRequest getLoginRequest(String userName) {
        String path = "users/search/getByLogin?login=" + userName;
        return getGetRequestWithAuth(path)
                .build();
    }

    // POST api/users
    public ANRequest getSignUpRequest(String userName, String password) {
        return getPostRequestWithFormData("users")
                .addUrlEncodeFormBodyParameter("login", userName)
                .addUrlEncodeFormBodyParameter("password", password)
                .build();
    }

    // {"login": userName, "password": password}
    private JSONObject createUserBody(String userName, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("login", userName);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
