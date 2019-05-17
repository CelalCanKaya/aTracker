package com.example.proje;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.spark.submitbutton.SubmitButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import es.dmoral.toasty.Toasty;

public class Settings extends MenuBar {

    public static final String FILE_NAME = "aTrackerDatas.csv";
    EditText heightText;
    EditText weightText;
    Integer heightTemp = -1;
    Integer weightTemp = -1;
    static Integer height = -1;
    static Integer weight = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        super.menuBar();
        heightText = (EditText) findViewById(R.id.heightText);
        weightText = (EditText) findViewById(R.id.weightText);
        SubmitButton submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
            }
        });
    }

    public void save(){
        if(!heightText.getText().toString().equals("") || !weightText.getText().toString().equals("")){
            heightTemp = Integer.parseInt(heightText.getText().toString());
            weightTemp = Integer.parseInt(weightText.getText().toString());
        }
        FileOutputStream fos = null;
        try{
            if((heightTemp>140 && heightTemp<210) && (weightTemp<160 && weightTemp>45)){
                height = heightTemp;
                weight = weightTemp;
                fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                String temp = height.toString() + " " + weight.toString();
                fos.write(temp.getBytes());
            }
            else{
                Toasty.error(getApplicationContext(), "Please Check Height And Weight Values.", Toast.LENGTH_SHORT, true).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
