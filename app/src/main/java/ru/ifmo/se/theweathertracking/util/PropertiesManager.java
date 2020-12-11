package ru.ifmo.se.theweathertracking.util;

import android.content.Context;
import android.content.SharedPreferences;

import ru.ifmo.se.theweathertracking.android.R;

public class PropertiesManager {
    private Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public PropertiesManager(Context ctx) {
        context = ctx;
        sharedPref = context.getSharedPreferences(
                context.getString(R.string.security_bucket_name), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void saveToken(String token, long expiration) {
        editor.putString(context.getString(R.string.security_token_value), token);
        editor.putLong(context.getString(R.string.security_token_expiration), expiration);
        editor.commit();
    }

    public void removeToken() {
        editor.remove(context.getString(R.string.security_token_value));
        editor.remove(context.getString(R.string.security_token_expiration));
        editor.commit();
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
