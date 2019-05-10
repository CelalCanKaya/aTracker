package com.example.proje;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainScreen extends MenuBar {

    int bpm = 0;
    String a = "";
    double length=0f;
    int stepcount=0;
    int[] x=new int[3];
    int count=0;
    int flag=0;
    int zmax=0;
    int zmin=0;
    int zmaxNorm=0;
    int zminNorm=0;
    int ymax=0;
    int ymin=0;
    int CountX=0;
    Thread thread1;
    ImageView isConnectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        super.menuBar();
        final TextView sCount = (TextView) findViewById(R.id.stepCounter);
        final TextView bpmCount = (TextView) findViewById(R.id.beatCount);
        isConnectedImage = (ImageView) findViewById(R.id.isConnected);
        final Button connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if(connection.btSocket==null) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.enable();
                    }
                }
            }
        });
        // Suanki adım sayısını almamız lazım.
        thread1 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                ArrayList arrList=new ArrayList<Integer>();
                if(connection.btSocket!=null) {
                    InputStream inputStream = null;
                    int avg=0;
                    int sum=0;
                    int xCount=0;
                    try {
                        inputStream = connection.btSocket.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    while(true){
                        try {
                            int ch;
                            ch=-1;
                            int zOld=0;
                            while (true) {
                                    if (connection.btSocket != null) {
                                        ch = inputStream.read();
                                    }
                                    if (ch == 44) {
                                        x[count] = Integer.parseInt(a);
                                        //System.out.println(a);
                                        count++;
                                        if (count == 3) {
                                            arrList.add(x[2]);
                                            count = 0;
                                        }
                                        a = "0";
                                    } else if (ch == 45)
                                        a = "-" + a;
                                    else if (ch == 65)
                                        break;
                                    else {
                                        if (ch == 46) {
                                            count = 0;
                                            bpm = Integer.parseInt(a);
                                            System.out.println(x[2]+" yy "+x[1] + " x " +x[0]);
                                            //System.out.println(x[1]);
                                           /* if(CountX<2){
                                                CountX++;
                                                if(CountX==2){
                                                    int avg=0;
                                                    int total=0;
                                                    for(int i = 0; i < arrList.size(); i++)
                                                    {
                                                        total = total + (int)arrList.get(i);
                                                        avg = total / arrList.size();
                                                    }
                                                    System.out.println("AVG BU KARDEŞİM"+avg);

                                                }
                                            }*/
                                            zmaxNorm=6600+600;
                                            zminNorm=6600-600;
                                            zmax=6600+1200;
                                            zmin=6600-1200;
                                            if (flag == 0 && ((x[2] > zmax || x[2] < zmin)||(zOld!=0&&x[2]-zOld>3000))) {
                                                stepcount += 1;
                                                zOld=x[2];
                                                System.out.println(stepcount);
                                                flag = 1;
                                            } else if (x[2] < zmaxNorm && x[2] > zminNorm) {
                                                flag = 0;
                                            }
                                            break;
                                        }
                                        if (ch >= 48 && ch <= 57) {
                                            a = a + Character.toString((char) ch);
                                        }
                                    }
                                if (ch == 65)
                                    break;

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            // text.setText(a);
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
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