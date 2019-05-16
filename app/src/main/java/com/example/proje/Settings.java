package com.example.proje;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;

public class Settings extends MenuBar {

    private static final String FILE_NAME = "aTrackerDatas.csv";
    EditText heightText;
    EditText weightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        super.menuBar();
        heightText = (EditText) findViewById(R.id.heightText);
        weightText = (EditText) findViewById(R.id.weightText);
    }

    public void save(View v){
        Integer height = Integer.parseInt(heightText.getText().toString());
    }

}
