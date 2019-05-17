package com.example.proje;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.spark.submitbutton.SubmitButton;

import org.w3c.dom.Text;

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
    TextView info;
    Integer heightTemp = -1;
    Integer weightTemp = -1;
    static Integer height = -1;
    static Integer weight = -1;
    Button feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        super.menuBar();
        info = findViewById(R.id.info);
        heightText = (EditText) findViewById(R.id.heightText);
        weightText = (EditText) findViewById(R.id.weightText);
        feedback = findViewById(R.id.feedback);
        SubmitButton submit = findViewById(R.id.submit);
        Typeface tf1 = Typeface.createFromAsset(getAssets(),  "fonts/Rounded_Elegance.ttf");
        weightText.setTypeface(tf1);
        heightText.setTypeface(tf1);
        info.setTypeface(tf1);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String[] TO = {"atrackerinfo@gmail.com"};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");

                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "aTracker FeedBack");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Your FeedBack");

                startActivity(Intent.createChooser(emailIntent, "Please Select An Mail App."));
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
