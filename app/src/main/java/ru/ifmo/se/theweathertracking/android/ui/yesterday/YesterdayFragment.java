package ru.ifmo.se.theweathertracking.android.ui.yesterday;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ru.ifmo.se.theweathertracking.android.R;

public class YesterdayFragment extends Fragment {

    private YesterdayViewModel yesterdayViewModel;
    private Button graphButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        yesterdayViewModel =
                new ViewModelProvider(this).get(YesterdayViewModel.class);
        View root = inflater.inflate(R.layout.fragment_yesterday, container, false);
        final TextView textView = root.findViewById(R.id.text_yesterday);
        yesterdayViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        graphButton = root.findViewById(R.id.btn_graph);

        graphButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putIntArray("values", new int[] {50, 120, 90});
            Navigation.findNavController(view)
                    .navigate(R.id.action_nav_yesterday_to_graphFragmnet, bundle);
        });

        return root;
    }
}