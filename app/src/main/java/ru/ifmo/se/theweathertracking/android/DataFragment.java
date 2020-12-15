package ru.ifmo.se.theweathertracking.android;

import android.app.ProgressDialog;
import androidx.fragment.app.Fragment;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import org.json.JSONObject;

public abstract class DataFragment extends Fragment {
    protected void loadData() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        sendRequest(progressDialog);
    }

    private void sendRequest(ProgressDialog progressDialog) {
        ANRequest request = getRequest();
        request.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                parseResponse(response);
                onGetDataSuccess();
                progressDialog.dismiss();
            }

            @Override
            public void onError(ANError anError) {
                if (anError.getErrorCode() == 403) {
                    unauthorizedResponse();
                } else {
                    onGetDataFailed("Error occurred");
                }
                progressDialog.dismiss();
            }
        });
    }

    protected abstract void parseResponse(JSONObject response);

    protected abstract ANRequest getRequest();

    protected abstract void unauthorizedResponse();

    protected abstract void onGetDataSuccess();

    protected abstract void onGetDataFailed(String message);
}
