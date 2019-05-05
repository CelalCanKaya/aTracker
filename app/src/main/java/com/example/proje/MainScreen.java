package com.example.proje;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainScreen extends MenuBar {

    int steps = 0;
    int bpm = 37;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        super.menuBar();
        final TextView sCount = (TextView) findViewById(R.id.stepCounter);
        final TextView bpmCount = (TextView) findViewById(R.id.beatCount);;
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
                                sCount.setText(Integer.toString(steps));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        Thread te = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(bpm==37){
                                    bpm=36;
                                }
                                else{
                                    bpm=37;
                                }
                                bpmCount.setText(Integer.toString(bpm)+" BPM");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
        te.start();
    }
}
