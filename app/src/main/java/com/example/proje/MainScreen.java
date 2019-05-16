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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    String url;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        super.menuBar();
        url = "http://40.117.95.148:9080/predict";
        final TextView sCount = (TextView) findViewById(R.id.stepCounter);
        final TextView bpmCount = (TextView) findViewById(R.id.beatCount);
        isConnectedImage = (ImageView) findViewById(R.id.isConnected);
        final Button connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();
                }
                else{
                    Toasty.info(getApplicationContext(), "Bluetooth Is Already Active!", Toast.LENGTH_SHORT, true).show();
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
                                    if (ch == 42)
                                        break;
                                    a = a + Character.toString((char) ch);
                                }
                            }
                            System.out.println(a);
                            try {
                                JSONObject jsonObject = new JSONObject(a);
                                JSONArray arrJson = jsonObject.getJSONArray("x_axis");
                                String[] arr = new String[arrJson.length()];
                                for(int i = 0; i < arrJson.length(); i++){
                                    arr[i] = arrJson.getString(i);
                                }
                                queue = Volley.newRequestQueue(getApplicationContext());
                                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                System.out.println("SERVER RESPONSE: " + response);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("ERROR");
                                            }
                                        }
                                );
                                objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                                        0,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(objectRequest);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            a="";
                        } catch (IOException e) {

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
        Thread control = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(50);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    if(connection.isBtConnected){
                                        isConnectedImage.setImageResource(R.drawable.connect);
                                    }
                                    else{
                                        isConnectedImage.setImageResource(R.drawable.disconnect);
                                    }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        thread1.start();
        t.start();
        control.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
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
                        new BTbaglan().execute();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
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
                if(connection.btSocket != null){
                    connection.btSocket.connect();
                }
                else{
                    ConnectSuccess = false;
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