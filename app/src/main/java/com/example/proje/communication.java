package com.example.proje;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class communication extends AppCompatActivity {

    String address=null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    Button veriAlici;
    String a = "";
    double length=0f;
    int stepcount=0;
    int[] x=new int[3];
    int count=0;
    BluetoothSocket btSocket = null;
    BluetoothDevice remoteDevice;
    BluetoothServerSocket mmServer;
    TextView text;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
        Intent newint= getIntent();
        address = newint.getStringExtra(bluetooth.EXTRA_ADRESS);
        veriAlici= (Button) findViewById(R.id.veriAl);
        text=(TextView) findViewById(R.id.bascam);
        text.setText("değişti");

        veriAlici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btSocket!=null) {
                    System.out.println("selam canım");
                    while(true){
                        try {
                            InputStream inputStream = btSocket.getInputStream();
                            int ch;
                            while (true) {
                                ch = inputStream.read();
                                //System.out.println(ch);
                                if (ch == 44) {
                                    x[count]=Integer.parseInt(a);
                                    count++;
                                    if(count==3)
                                        count=0;
                                    a="";
                                }
                                else if(ch == 65)
                                    break;
                                else {

                                    if (ch == 10) {
                                        count=0;
                                       // System.out.println(a);
                                        length = length + Math.sqrt(x[0] * x[0] + x[1] * x[1] + x[2] * x[2]);
                                        if (length >= 10) {
                                            length = length - 10;
                                            stepcount += 1;
                                        }
                                        System.out.println(stepcount);
                                        break;
                                    }

                                    a = a + Character.toString((char) ch);
                                }
                            }

                            try {
                                text.setText(a);
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            a = "";

                            if(ch == 65)
                                break;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });

        new BTbaglan().execute();

         /* byte[] buffer = new byte[1024];
                    int bytes;

        try {
            bytes= btSocket.getInputStream().read(buffer);
            System.out.println(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }






    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Disconnect();
    }

    private void Disconnect() {
        if(btSocket != null){
            try{
                btSocket.close();
            } catch (IOException e) {
                // msg("ERROR");
            }
        }
        finish();
    }


    @SuppressLint("StaticFieldLeak")
    private class BTbaglan extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(communication.this , "Baglanıyor...", "Lütfen Bekleyin");
        }

        // https://gelecegiyazanlar.turkcell.com.tr/konu/android/egitim/android-301/asynctask
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
                Toast.makeText(getApplicationContext(), "Bağlantı Hatası Tekrar Deneyin", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                //   msg("Baglantı Basarılı");
                Toast.makeText(getApplicationContext(), "Bağlantı Başarılı", Toast.LENGTH_SHORT).show();

                isBtConnected = true;
            }
            progress.dismiss();
        }

    }
   /* private class Client extends Thread{
        private BluetoothDevice device;
        private BluetoothSocket socket;


    }
    private class SendReceive extends Thread{
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive(BluetoothSocket socket){
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;

            try {
                tempIn=bluetoothSocket.getInputStream();
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream=tempIn;
            outputStream=tempOut;

        }

        public void run(){
            byte[] buffer=new byte[1024];
            int bytes;

            while(true)
            {
                try {
                    bytes=inputStream.read(buffer);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/
}
