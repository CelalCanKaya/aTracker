package com.example.proje;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Set;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;


public class MainScreen extends MenuBar {

    String bpm = "";
    String a = "";
    double length=0f;
    static int stepcount=0;
    int count=0;
    int flag=0;
    int CountX=0;
    Thread thread1;
    Thread control;
    Thread t;
    ImageView isConnectedImage;
    public AlertDialog alertDia;
    String url;
    RequestQueue queue;
    TextView currentState;
    TextView calories;
    double res = 0f;
    boolean stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        super.menuBar();
        stop=false;
        url = "http://40.117.95.148:9080/predict";
        final TextView sCount = (TextView) findViewById(R.id.stepCounter);
        final TextView bpmCount = (TextView) findViewById(R.id.beatCount);
        isConnectedImage = (ImageView) findViewById(R.id.isConnected);
        final Button connectButton = findViewById(R.id.connectButton);
        Typeface tf1 = Typeface.createFromAsset(getAssets(),  "fonts/Rounded_Elegance.ttf");
        currentState = findViewById(R.id.currentState);
        calories = findViewById(R.id.calories);
        currentState.setTypeface(tf1);
        calories.setTypeface(tf1);
        bpmCount.setTypeface(tf1);
        sCount.setTypeface(tf1);
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
                    while(!stop){
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
                                JSONArray arrJsonX = jsonObject.getJSONArray("x_axis");
                                JSONArray arrJsonY = jsonObject.getJSONArray("y_axis");
                                JSONArray arrJsonZ = jsonObject.getJSONArray("z_axis");
                                String[] arrX = new String[arrJsonX.length()];
                                String[] arrY = new String[arrJsonY.length()];
                                String[] arrZ = new String[arrJsonZ.length()];
                                for(int i = 0; i < arrJsonX.length(); i++){
                                    arrX[i] = arrJsonX.getString(i);
                                    arrY[i] = arrJsonY.getString(i);
                                    arrZ[i] = arrJsonZ.getString(i);
                                    bpm=jsonObject.get("pulse_meter").toString();

                                    double x,y,z;
                                    x=Double.parseDouble(arrX[i])/8192;
                                    y=Double.parseDouble(arrY[i])/8192;
                                    z=Double.parseDouble(arrZ[i])/8192;
                                    res=Math.sqrt(x*x+y*y+z*z);
                                    //System.out.println("x celal"+arrX[i]+"-"+x+" y can"+arrY[i]+" z kaya"+arrZ[i]+"Sonuc"+res);
                                    if(x>0&&flag==0&&res>0.95f){
                                        stepcount++;
                                        flag=1;
                                    }
                                    else if(x>0&&res<0.85f){
                                        flag=0;
                                    }


                                }
                                queue = Volley.newRequestQueue(getApplicationContext());
                                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                int state = 0;
                                                try {
                                                    state = Integer.parseInt(response.getString("Result"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                if(state==0){
                                                    currentState.setText("WALKING");
                                                }
                                                else if(state==1){
                                                    currentState.setText("RUNNING");
                                                }
                                                else if(state==2){
                                                    currentState.setText("STAIRING UP");
                                                }
                                                else if(state==3){
                                                    currentState.setText("STAIRING DOWN");
                                                }
                                                else if(state==4){
                                                    currentState.setText("JUMPING");
                                                }
                                                else if(state==5){
                                                    currentState.setText("STANDING");
                                                }
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
                            Thread.sleep(20);
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
                    while (!stop) {
                        Thread.sleep(50);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String stee=""+stepcount;
                                sCount.setText(stee);
                                NumberFormat formatter = new DecimalFormat("#0.0000");
                                if(Settings.height>=180){
                                    double temp = stepcount* (0.028 + (Settings.weight-45)*0.0005);
                                    calories.setText("Burnt Calories: " + formatter.format(temp) + " cal");
                                }
                                else if(Settings.height<180 && Settings.weight>165){
                                    double temp = stepcount* (0.025 + (Settings.weight-45)*0.0005);
                                    calories.setText("Burnt Calories: " + formatter.format(temp) + " cal");
                                }
                                else if(Settings.height<180 && Settings.weight>165){
                                    double temp = stepcount* (0.023 + (Settings.weight-45)*0.0005);
                                    calories.setText("Burnt Calories: " + formatter.format(temp) + " cal");
                                }
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
                    while (!stop) {
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
        stop=true;
        unregisterReceiver(mReceiver);
        super.onDestroy();
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
                Toasty.error(getApplicationContext(), "Cannot Connect With ESP32.", Toast.LENGTH_SHORT, true).show();
            } else {
                Toasty.success(getApplicationContext(), "Connected With ESP32.", Toast.LENGTH_SHORT, true).show();
                connection.isBtConnected=true;
            }
            alertDia.dismiss();
        }

    }
}