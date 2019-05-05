package com.example.proje;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class MainScreen extends MenuBar {

    int steps = 0;
    String address="3C:71:BF:AA:DB:F2";
    private ProgressDialog progress;
    public AlertDialog alertDia;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        super.menuBar();
        final TextView sCount = (TextView) findViewById(R.id.stepCounter);
        new BTbaglan().execute();
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

                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice cihaz = myBluetooth.getRemoteDevice(address);
                    System.out.println(address);
                    btSocket = cihaz.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
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
                // msg("Baglantı Hatası, Lütfen Tekrar Deneyin");
                Toasty.error(getApplicationContext(), "ESP32 İle Bağlantı Kurulamadı.", Toast.LENGTH_SHORT, true).show();
            } else {
                //   msg("Baglantı Basarılı");
                Toasty.success(getApplicationContext(), "ESP32 İle Bağlantı Kuruldu.", Toast.LENGTH_SHORT, true).show();
                isBtConnected = true;
            }
            alertDia.dismiss();
        }

    }
}

