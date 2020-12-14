package ru.ifmo.se.theweathertracking.android.ui.now;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ru.ifmo.se.theweathertracking.android.R;

public class NowFragment extends Fragment {

    private NowViewModel nowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        nowViewModel =
                new ViewModelProvider(this).get(NowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_now, container, false);
//        final TextView textView = root.findViewById(R.id.text_now);
//        nowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}