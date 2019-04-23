package com.example.proje;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class ChartScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_screen);

        ChartFragment chartFragment = new ChartFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, chartFragment).commit();
    }
}
