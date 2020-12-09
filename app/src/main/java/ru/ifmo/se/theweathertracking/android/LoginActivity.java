package ru.ifmo.se.theweathertracking.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import org.json.JSONException;
import org.json.JSONObject;
import ru.ifmo.se.theweathertracking.api.ProfileController;
import ru.ifmo.se.theweathertracking.util.PropertiesManager;

public class LoginActivity extends BaseActivity {
    private final String tag = "Main activity";
    private ProfileController profileController;
    private PropertiesManager propertiesManager;

    @Override
    protected String getTag() {
        return this.tag;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.app_name;
    }

    Button submitButton;
    EditText email, password;
    TextView signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        submitButton = findViewById(R.id.btn_login);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        signupLink = findViewById(R.id.link_signup);

        submitButton.setOnClickListener((View v) -> login());

        Context ctx = getApplicationContext();
        profileController = new ProfileController(ctx);
        propertiesManager = new PropertiesManager(ctx);
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        submitButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        // TODO: Implement your own authentication logic here.

        profileController.getLoginRequest(emailText, passwordText)
                .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject entity = response.getJSONObject("Entity");
                    String token = entity.getString("Token");
                    long expiration = entity.getLong("Expires");
                    onLoginSuccess(token, expiration);
                } catch (JSONException e) {
                    onLoginFailed();
                    e.printStackTrace();
                }
                finally {
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onError(ANError error) {
                onLoginFailed();
                progressDialog.dismiss();
            }
        });
    }

    public void onLoginSuccess(String token, long expiration) {
        submitButton.setEnabled(true);
        propertiesManager.saveToken(token, expiration);
        if (propertiesManager.hasValidToken()) {
            Toast.makeText(getBaseContext(), "Login succeed", Toast.LENGTH_LONG).show();
        }
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        submitButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        if (emailText.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (passwordText.isEmpty() || passwordText.length() < 4 || passwordText.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }
}
