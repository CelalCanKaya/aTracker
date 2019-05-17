package com.example.proje;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
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
        load();
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
                load();
                if (btSocket == null || !isBtConnected) {
                    System.out.println("BASAMAK 1 OK");
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    System.out.println("BASAMAK 2 OK");
                    BluetoothDevice cihaz = myBluetooth.getRemoteDevice(address);
                    System.out.println("BASAMAK 3 OK");
                    btSocket = cihaz.createInsecureRfcommSocketToServiceRecord(myUUID);
                    System.out.println("BASAMAK 4 OK");
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    System.out.println("BASAMAK 5 OK");
                    btSocket.connect();
                    System.out.println("BASAMAK 6 OK");
                }
            } catch (IOException e) {
                try {
                    btSocket.getInputStream().close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    btSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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

    public void load(){
        FileInputStream fis = null;
        try {
            fis = openFileInput(Settings.FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text = br.readLine();
            String[] splited = text.split("\\s+");
            Settings.height = Integer.parseInt(splited[0]);
            Settings.weight = Integer.parseInt(splited[1]);
        } catch (FileNotFoundException e) {
            Toasty.error(getApplicationContext(), "Cannot retrieve the height and weight data!", Toast.LENGTH_SHORT, true).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}