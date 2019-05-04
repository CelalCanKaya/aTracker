package com.example.proje;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class bluetooth extends MenuBar {


    BluetoothAdapter myBluetooth;

    Button forList, Toggle;
    private Set<BluetoothDevice> cihaz;
    ListView theList;
    public static String EXTRA_ADRESS = "device_address";
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        super.menuBar();

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        Toggle = (Button) findViewById(R.id.button1);
        forList = (Button) findViewById(R.id.liste);
        theList = (ListView) findViewById(R.id.listeee);
        String address2="3C:71:BF:AA:DB:F2";
        Intent comintent2 = new Intent(bluetooth.this, communication.class);
        comintent2.putExtra(EXTRA_ADRESS, address2);
        startActivity(comintent2);


        Toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toogleBluetooth();
            }
        });

        forList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listdevice();
            }


        });

    }

    private void listdevice() {
        cihaz = myBluetooth.getBondedDevices();
        ArrayList listem = new ArrayList();

        if (cihaz.size() > 0) {
            for (BluetoothDevice bt : cihaz) {
                listem.add(bt.getName() + "\n" + bt.getAddress());
            }
        } else {
            Toast.makeText(getApplicationContext(), "cihaz yok eşleşmiş", Toast.LENGTH_SHORT).show();
        }
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listem);
        theList.setAdapter(adapter);
        theList.setOnItemClickListener(selectDevice);
    }

    private void toogleBluetooth() {
        if (myBluetooth == null) {
            Toast.makeText(getApplicationContext(), "bluetooth yok biladerim", Toast.LENGTH_SHORT).show();
        }
        if (!myBluetooth.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);
        }
        if (myBluetooth.isEnabled()) {
            myBluetooth.disable();
        }

    }

    public AdapterView.OnItemClickListener selectDevice = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);

            Intent comintent = new Intent(bluetooth.this, communication.class);
            comintent.putExtra(EXTRA_ADRESS, address);
            startActivity(comintent);

        }
    };
}