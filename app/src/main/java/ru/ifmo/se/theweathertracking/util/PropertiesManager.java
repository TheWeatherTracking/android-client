package ru.ifmo.se.theweathertracking.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.UnsupportedEncodingException;

import ru.ifmo.se.theweathertracking.android.R;

public class PropertiesManager {
    private Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public static long DefaultExpiration = 86400000;

    public PropertiesManager(Context ctx) {
        context = ctx;
        sharedPref = context.getSharedPreferences(
                context.getString(R.string.security_bucket_name), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void saveDevice(String device) {
        editor.putString(context.getString(R.string.security_device_value), device);
        editor.apply();
    }

    public void removeDevice() {
        editor.remove(context.getString(R.string.security_device_value));
        editor.apply();
    }

    public String getDevice() {
        String result = sharedPref.getString(context.getString(R.string.security_device_value), null);
        return result;
    }

    public boolean hasDevice() {
        String device = getDevice();
        return device != null && !device.equals("");
    }

    public void saveToken(String login, String password, long expiration) {
        String uncodedToken = login + ":" + password;
        byte[] data = new byte[0];
        try {
            data = uncodedToken.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String token = Base64.encodeToString(data, Base64.DEFAULT).replace("\n", "");
        editor.putString(context.getString(R.string.security_token_value), token);
        editor.putLong(context.getString(R.string.security_token_expiration), expiration);
        editor.apply();
    }

    public void removeToken() {
        editor.remove(context.getString(R.string.security_token_value));
        editor.remove(context.getString(R.string.security_token_expiration));
        editor.apply();
    }

    public String getToken() {
        String result = null;
        if (hasValidToken()) {
            result = sharedPref.getString(context.getString(R.string.security_token_value), null);
        }
        return result;
    }

    public boolean hasValidToken() {
        boolean result = false;

        if (sharedPref.contains(context.getString(R.string.security_token_value)) &&
                sharedPref.contains(context.getString(R.string.security_token_expiration))) {
            long expirationTimeStamp = sharedPref.getLong(context.getString(R.string.security_token_expiration), 0);
            if (expirationTimeStamp > System.currentTimeMillis()) {
                result = true;
            }
        }

        return result;
    }
}
