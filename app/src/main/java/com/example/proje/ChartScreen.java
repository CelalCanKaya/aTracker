package com.example.proje;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.Chart;

public class ChartScreen extends MenuBar {

    Switch switch1;
    static TextView jumpText;
    static TextView walkText;
    static TextView stairsText;
    static TextView runText;
    TextView todayAct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_screen);
        super.menuBar();
        jumpText = findViewById(R.id.jumpText);
        walkText = findViewById(R.id.walkText);
        stairsText = findViewById(R.id.stairsText);
        runText = findViewById(R.id.runText);
        switch1 = findViewById(R.id.switch1);
        todayAct = findViewById(R.id.todayAct);
        Typeface tf1 = Typeface.createFromAsset(getAssets(),  "fonts/Rounded_Elegance.ttf");
        jumpText.setTypeface(tf1);
        walkText.setTypeface(tf1);
        stairsText.setTypeface(tf1);
        runText.setTypeface(tf1);
        todayAct.setTypeface(tf1);
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
