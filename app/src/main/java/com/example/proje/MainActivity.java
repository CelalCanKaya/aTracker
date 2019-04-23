package com.example.proje;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    BluetoothAdapter myBluetooth;
    Button celalButton,celalListButton;
    ListView celalList;
    private Set<BluetoothDevice> celalDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myBluetooth=BluetoothAdapter.getDefaultAdapter();
        celalButton=(Button) findViewById(R.id.celalFU);
        celalListButton=(Button) findViewById(R.id.celalCan);
        celalList= (ListView) findViewById(R.id.celalList);


        celalButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               toggleBluetooth();
                                           }
                                       });
        celalListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listdevice();
            }
        });
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();

    }

    private void listdevice() {
        celalDevices=myBluetooth.getBondedDevices();
        ArrayList list =new ArrayList();
        if(celalDevices.size()>0){
            for(BluetoothDevice bt: celalDevices){
                list.add(bt.getName()+"\n"+bt.getAddress());
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Eşleşmiş cihaz yok",Toast.LENGTH_SHORT).show();
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        celalList.setAdapter(adapter);

    }

    private void toggleBluetooth() {
        if(myBluetooth==null){
            Toast.makeText(getApplicationContext(),"Blueetooth Cihazı Yok celal",Toast.LENGTH_SHORT).show();
        }
        if(!myBluetooth.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

        }
        if(myBluetooth.isEnabled()){

            myBluetooth.disable();
        }
    }


}
