package ru.ifmo.se.theweathertracking.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import okhttp3.Response;
import ru.ifmo.se.theweathertracking.api.UsersController;

public class SignUpActivity extends BaseActivity {
    private String tag = "Sign up activity";
    private UsersController usersController;

    @Override
    protected String getTag() {
        return this.tag;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_signup;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.app_name;
    }

    EditText email, password, confirmedPassword;
    Button submitButton;
    TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        confirmedPassword = findViewById(R.id.input_confirmPassword);
        submitButton = findViewById(R.id.btn_signup);
        loginLink = findViewById(R.id.link_login);

        Context ctx = getApplicationContext();
        usersController = new UsersController(ctx);

        submitButton.setOnClickListener((View v) -> signUp());
        loginLink.setOnClickListener((View v) -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void signUp() {
        if (!validate()) {
            onSignUpFailed("Fill all the fields");
            return;
        }
        submitButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String emailString = this.email.getText().toString();
        String passwordString = this.password.getText().toString();
        String confirmedPasswordString = confirmedPassword.getText().toString();

        ANRequest request = usersController.getSignUpRequest(emailString, passwordString);
        request.getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response.isSuccessful()) {
                            onSignUpSuccess();
                        } else {
                            onSignUpFailed("Sign up failed");
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        onSignUpFailed("Sign up failed");
                        progressDialog.dismiss();
                    }
                });
    }

    public void onSignUpSuccess() {
        Toast.makeText(getBaseContext(), "Sign up succeeded", Toast.LENGTH_LONG).show();
        submitButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignUpFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        submitButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String emailText = this.email.getText().toString();
        String passwordText = this.password.getText().toString();
        String confirmedPasswordText = confirmedPassword.getText().toString();

        if (emailText.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            this.email.setError("Enter a valid email address");
            valid = false;
        } else {
            this.email.setError(null);
        }

        if (passwordText.isEmpty() || passwordText.length() < 4 || passwordText.length() > 10) {
            this.password.setError("Password should be between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            this.password.setError(null);
        }

        if (confirmedPasswordText.isEmpty() || confirmedPasswordText.length() < 4 || confirmedPasswordText.length() > 10 || !(confirmedPasswordText.equals(passwordText))) {
            confirmedPassword.setError("Passwords do not match");
            valid = false;
        } else {
            confirmedPassword.setError(null);
        }

        return valid;
    }
}
