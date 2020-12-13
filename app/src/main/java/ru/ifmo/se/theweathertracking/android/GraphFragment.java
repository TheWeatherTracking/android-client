package ru.ifmo.se.theweathertracking.android;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_graph, container, false);

        BarChart chart = (BarChart) root.findViewById(R.id.chart);
        BarData data = new BarData(getDataSet());
        chart.setData(data);

        Description desc = new Description();
        desc.setText("Example");
        desc.setTextAlign(Paint.Align.CENTER);
        chart.setDescription(desc);

        chart.animateXY(2000, 2000);
        chart.invalidate();

        return root;
    }

    private ArrayList getDataSet() {
        int[] values = getArguments().getIntArray("values");
        ArrayList valueSet = new ArrayList();
        int i = 1;
        for (int value : values) {
            BarEntry v = new BarEntry(i++, value);
            v.setData("day");
            valueSet.add(v);
        }

        BarDataSet barDataSet = new BarDataSet(valueSet, "Temperature");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList dataSet = new ArrayList();
        dataSet.add(barDataSet);
        return dataSet;
    }
}
