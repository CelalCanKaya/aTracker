package com.example.proje;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainScreen extends MenuBar {

    int steps = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        super.menuBar();
        final TextView sCount = (TextView) findViewById(R.id.stepCounter);
        // Suanki adım sayısını almamız lazım.
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                steps++;
                                sCount.setText(Integer.toString(steps)+"\nSteps");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }
}

