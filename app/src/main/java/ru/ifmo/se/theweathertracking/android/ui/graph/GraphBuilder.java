package ru.ifmo.se.theweathertracking.android.ui.graph;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collection;

public class GraphBuilder {
    public void fillBarChart(BarChart chart, String graphLabel, Collection<String> dataLabels, Collection<Integer> dataValues, int color)
    {
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new LabelFormatter(dataLabels.toArray(new String[dataLabels.size()])));

        ArrayList<BarEntry> values = new ArrayList<>();
        int i = 1;
        for(int value : dataValues) {
            values.add(new BarEntry(i++, value));
        }

        BarDataSet dataSet = new BarDataSet(values, graphLabel);
        dataSet.setColor(color, 255);
        BarData data = new BarData(dataSet);
        chart.setData(data);
        chart.animateXY(2000, 2000);
        chart.invalidate();
    }

    public class LabelFormatter extends ValueFormatter implements IAxisValueFormatter {
        private final String[] mLabels;

        public LabelFormatter(String[] labels) {
            mLabels = labels;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            int index = (int)value;
            index = index >= mLabels.length ? 0 : index;
            return mLabels[index];
        }
    }
}
