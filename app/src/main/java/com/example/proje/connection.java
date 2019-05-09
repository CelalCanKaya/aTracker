package com.example.proje;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class connection extends AppCompatActivity {

    static String address="84:0D:8E:2C:03:96";
    static String address2="3C:71:BF:AA:DB:F2";
    public AlertDialog alertDia;
    static BluetoothAdapter myBluetooth = null;
    static BluetoothSocket btSocket = null;
    static boolean isBtConnected = false;
    final static UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new BTbaglan().execute();
    }


    @SuppressLint("StaticFieldLeak")
    private class BTbaglan extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            alertDia = new SpotsDialog.Builder().setContext(connection.this).build();
            alertDia.setMessage("Connecting With aTracker.");
            alertDia.show();
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {

                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice cihaz = myBluetooth.getRemoteDevice(address);
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
                Intent menu = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(menu);
                Toasty.error(getApplicationContext(), "ESP32 İle Bağlantı Kurulamadı.", Toast.LENGTH_SHORT, true).show();
                btSocket=null;
                isBtConnected = false;
                finish();
            } else {
                Intent menu = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(menu);
                Toasty.success(getApplicationContext(), "ESP32 İle Bağlantı Kuruldu.", Toast.LENGTH_SHORT, true).show();
                finish();
                isBtConnected = true;
            }
            alertDia.dismiss();
        }

    }
}