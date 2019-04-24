package com.example.proje;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainScreen extends MenuBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        super.menuBar();
    }

}
