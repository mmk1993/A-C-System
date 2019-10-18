package com.example.acsystem;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class ManageConnection extends AppCompatActivity {

    private static final String TAG = "Manage_Connection";
    private final BluetoothSocket mmSocket = ConnectThread.mmSocket;
    private static OutputStream mmOutStream = null;
    private static InputStream mmInputStream = null;
    private Handler mHandler;
    private byte[] mmBuffer; // mmBuffer store for the stream
    TextView temp;



    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_bt);
        Button btnOn = (Button) findViewById(R.id.btnOn);
        Button btnOff = (Button) findViewById(R.id.btnOff);
        Button btndc = (Button) findViewById(R.id.btndisconnect);
        temp = (TextView) findViewById(R.id.currentTemp);
        setIOStream();

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                char text= 'o';
                sendOnOff(text);
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                char text = 'f';
                sendOnOff(text);
            }
        });

        btndc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnect();
            }
        });

        ReadThread read = new ReadThread(mmInputStream, temp);
        read.start();


    }

    public void disconnect(){
        char text = 'd';
        sendOnOff(text);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.thread.cancel();
            }
        }, 1000);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ManageConnection.this, MainActivity.class);
                startActivity(intent);
            }
        }, 2000);

    }

    public static void sendOnOff(char text){
        Log.d(TAG, "Send on.");
        write(text);

    }

    public static void write(char text) {

        Log.d(TAG, "write: Writing to outputstream: " + text);
        try {
            mmOutStream.write(text);
        } catch (IOException e) {
            Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
        }
    }

    public void setIOStream(){
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = mmSocket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }
        mmInputStream= tmpIn;
        mmOutStream = tmpOut;
    }

}
