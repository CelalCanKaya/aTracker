package com.example.proje;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class ChartScreen extends MenuBar {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_screen);
        super.menuBar();
        ChartFragment chartFragment = new ChartFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, chartFragment).commit();
    }
}
