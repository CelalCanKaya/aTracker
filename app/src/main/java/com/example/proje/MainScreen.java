package com.example.proje;

import android.os.Bundle;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;


public class MainScreen extends MenuBar {

    int bpm = 0;
    String a = "";
    double length=0f;
    int stepcount=0;
    int[] x=new int[3];
    int count=0;
    Thread thread1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        super.menuBar();
        final TextView sCount = (TextView) findViewById(R.id.stepCounter);
        final TextView bpmCount = (TextView) findViewById(R.id.beatCount);
        // Suanki adım sayısını almamız lazım.
        thread1 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                if(connection.btSocket!=null) {
                    while(true){
                        try {
                            InputStream inputStream = connection.btSocket.getInputStream();
                            int ch;
                            while (true) {
                                ch = inputStream.read();
                                // System.out.println(ch);
                                if (ch == 44) {
                                    x[count]=Integer.parseInt(a);
                                    System.out.println(a);
                                    count++;
                                    if(count==3)
                                        count=0;
                                    a="0";
                                }
                                else if(ch == 65)
                                    break;
                                else {

                                    if (ch == 46) {
                                        count=0;
                                        bpm=Integer.parseInt(a);
                                        // System.out.println(a);
                                        length =  Math.sqrt(x[0] * x[0] + x[1] * x[1] + x[2] * x[2]);
                                        if (length >= 10) {
                                            stepcount += 1;

                                        }
                                        System.out.println(stepcount);
                                        break;
                                    }
                                    if(ch>=48&&ch<=57)
                                        a = a + Character.toString((char) ch);
                                    else
                                        a = 0+a;
                                }
                            }

                            try {
                                // text.setText(a);
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if(ch == 65)
                                break;
                            a = "";


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String stee=""+stepcount;
                                sCount.setText(stee);
                                String bpee=bpm+" BPM";
                                bpmCount.setText(bpee);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        thread1.start();
        t.start();
    }
}
