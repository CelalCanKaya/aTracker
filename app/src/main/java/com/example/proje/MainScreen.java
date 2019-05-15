package com.example.proje;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;


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
    public AlertDialog alertDia;

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
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
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
                                if (connection.isBtConnected) {
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
                                        double ax,ay,az;
                                        ax=(double)x[0]/8192;
                                        ay=(double)x[1]/8192;
                                        az=(double)x[2]/8192;
                                        double kar = ax * ax + ay * ay + az * az;
                                        double kok=(Math.sqrt(kar));
                                        System.out.println("kare"+kok);
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
                                        if (flag == 0 && (kok>1)) {
                                            stepcount += 1;
                                            zOld=x[2];
                                            System.out.println(stepcount);
                                            flag = 1;
                                        } else if (kok<0.7f) {
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
                            Thread.sleep(40);
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

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (bluetoothState) {
                    case BluetoothAdapter.STATE_ON:
                        try {
                            if(connection.btSocket != null){
                                connection.btSocket.connect();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        connection.isBtConnected=true;
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        /*try {
                            connection.btSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        connection.btSocket = null;*/
                        connection.isBtConnected=false;
                        break;
                }
            }
        }
    };

    @SuppressLint("StaticFieldLeak")
    private class BTbaglan extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            alertDia = new SpotsDialog.Builder().setContext(MainScreen.this).build();
            alertDia.setMessage("Connecting With aTracker.");
            alertDia.show();
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (connection.btSocket == null || !connection.isBtConnected) {
                    System.out.println("BASAMAK 1 BAŞARILI.");
                    connection.myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    System.out.println("BASAMAK 2 BAŞARILI.");
                    BluetoothDevice cihaz = connection.myBluetooth.getRemoteDevice(connection.address);
                    System.out.println("BASAMAK 3 BAŞARILI.");
                    connection.btSocket = cihaz.createInsecureRfcommSocketToServiceRecord(connection.myUUID);
                    System.out.println("BASAMAK 5 BAŞARILI.");
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    System.out.println("BASAMAK 6 BAŞARILI.");
                    connection.btSocket.connect();
                    System.out.println("BASAMAK 7 BAŞARILI.");
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!ConnectSuccess) {
                Toasty.error(getApplicationContext(), "ESP32 İle Bağlantı Kurulamadı.", Toast.LENGTH_SHORT, true).show();
            } else {
                Toasty.success(getApplicationContext(), "ESP32 İle Bağlantı Kuruldu.", Toast.LENGTH_SHORT, true).show();
                connection.isBtConnected=true;
            }
            alertDia.dismiss();
        }

    }
}