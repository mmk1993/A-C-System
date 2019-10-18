package com.example.acsystem;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

public class ReadThread extends Thread {
    private InputStream mmInStream = null;
    TextView temp;

    public ReadThread(InputStream instream, TextView text){
        mmInStream = instream;
        temp = text;


    }

    public void run(){
        Log.d(TAG, "I AM RUNNING");

                byte[] buffer = new byte[1024];  // buffer store for the stream
                int bytes; // bytes returned from read()

                // Keep listening to the InputStream until an exception occurs
                while (true) {
                    // Read from the InputStream
                    try {
                        bytes = mmInStream.read(buffer);
                        String incomingMessage = new String(buffer, 0, bytes);
                        Log.d(TAG, "InputStream: " + incomingMessage);
                        String temperature = ((incomingMessage) + "\u2109");
                        Log.d(TAG, "Temp is " + temperature);
                        temp.setText(temperature);
                    } catch (IOException e) {
                        Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage());
                        break;
                    }
                }


    }
}
