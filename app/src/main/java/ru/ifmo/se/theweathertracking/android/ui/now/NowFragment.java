package ru.ifmo.se.theweathertracking.android.ui.now;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import org.json.JSONObject;
import ru.ifmo.se.theweathertracking.android.R;
import ru.ifmo.se.theweathertracking.api.TelemetriesController;
import ru.ifmo.se.theweathertracking.api.model.TelemetryModel;

public class NowFragment extends Fragment {
    private TelemetriesController telemetriesController;
    private TelemetryModel telemetryModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_now, container, false);
        telemetriesController = new TelemetriesController(getContext());
        telemetryModel = new TelemetryModel();

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        telemetriesController.getCurrentTelemetry()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        telemetryModel = new TelemetryModel(response);
                        onGetDataSuccess();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 403) {
                            unauthorizedRequest();
                        } else {
                            onGetDevicesFailed("Error occurred");
                        }
                        progressDialog.dismiss();
                    }
                });

        return root;
    }

    public void onGetDataSuccess()
    {
        //display info from telemetry model
    }

    private void unauthorizedRequest() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_nav_now_to_loginActivity);
    }

    public void onGetDevicesFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
