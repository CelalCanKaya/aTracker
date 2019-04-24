package com.example.proje;


import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    private BarChart bar;

    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chart, container, false);

        bar = view.findViewById(R.id.barChart);
        getChart();

        return view;
    }

    private void getChart(){
        List<BarEntry> barEntries = new ArrayList<>();
        for(int i=0; i<7; i++){
            barEntries.add(new BarEntry(i,i));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Steps");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        bar.setVisibility(View.VISIBLE);
        bar.animateY(2000);
        bar.setData(barData);
        bar.setFitBars(true);
        bar.getAxisLeft().setDrawGridLines(false);
        bar.getXAxis().setDrawGridLines(false);
        XAxis xAxis = bar.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Mon","Tue","Wed","Thu","Fri", "Sat", "Sun"}));
        Description description = new Description();
        description.setText("Steps Per Day");
        bar.setDragEnabled(false);
        bar.setScaleEnabled(false);
        bar.setDescription(description);
        bar.invalidate();
    }


}
