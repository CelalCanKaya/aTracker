package com.example.proje;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.Chart;

public class ChartScreen extends MenuBar {

    Switch switch1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_screen);
        super.menuBar();
        switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ChartFragment.chartNum=1;
                    try {
                        ChartFragment.getChart(ChartFragment.chartNum);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    ChartFragment.chartNum=0;
                    try {
                        ChartFragment.getChart(ChartFragment.chartNum);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        ChartFragment chartFragment = new ChartFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, chartFragment).commit();
    }
}
