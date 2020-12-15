package ru.ifmo.se.theweathertracking.android;

import android.app.ProgressDialog;
import androidx.fragment.app.Fragment;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.ifmo.se.theweathertracking.api.model.TelemetryDataSetViewModel;
import ru.ifmo.se.theweathertracking.api.model.TelemetryModel;

public abstract class TelemetryFragment extends Fragment {
    protected TelemetryDataSetViewModel telemetryDataSetViewModel;
    private int pageLoaded;
    private int pageCount;

    protected void loadTelemetryData() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        pageLoaded = 0;
        sendRequest(progressDialog, -1, 1);
    }

    private void sendRequest(ProgressDialog progressDialog, int size, int page) {
        if (size > 0 && 1000*page < size)
        {
            sendRequest(progressDialog, size, page+1);
        }

        ANRequest request = getRequest(Math.min(size, 1000), page-1);
        request.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (size == -1)
                    {
                        int elementsCount = response
                                .getJSONObject("page")
                                .getInt("totalElements");
                        sendRequest(progressDialog, elementsCount, page);
                    } else {
                        JSONArray jsonArray = response
                                .getJSONObject("_embedded")
                                .getJSONArray("telemetries");
                        pageCount = response
                                .getJSONObject("page")
                                .getInt("totalPages");
                        if (response.length() == 0) {
                            onGetDataFailed("No data available");
                        }
                        TelemetryModel[] array = TelemetryModel.getTelemetryArray(jsonArray);
                        telemetryDataSetViewModel.addTelemetries(array);
                        pageLoaded++;
                        if (pageLoaded >= pageCount)
                        {
                            onGetDataSuccess();
                            progressDialog.dismiss();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    onGetDataFailed("Error occurred");
                    progressDialog.dismiss();
                }
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

    protected abstract ANRequest getRequest(int size, int page);

    protected abstract void unauthorizedResponse();

    protected abstract void onGetDataSuccess();

    protected abstract void onGetDataFailed(String message);
}
