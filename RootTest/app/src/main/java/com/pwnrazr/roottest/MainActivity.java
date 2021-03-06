package com.pwnrazr.roottest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView debugText = findViewById(R.id.debugText);
        TextView cmdText = findViewById(R.id.cmdText);

        debugText.setText("No error");

        Context context = getApplicationContext();

        Process p;
        try {
            p = Runtime.getRuntime().exec("su -c mount -o rw,remount / && echo tested > /system/testenment && rm /system/testenment && mount -o ro,remount / && cat /proc/version");
            p.waitFor();
            //debugText.setText(Integer.toString(p.exitValue()));
            //Get cmd output
            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(p.getInputStream()));

            // Grab the results
            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {    //append chars from InputStream
                log.append(line + "\n");
            }

            cmdText.setText(log.toString());    //update text

            if(p.exitValue()==0)
            {
            debugText.setText("Device is rooted!");
            Toast toast = Toast.makeText(context, "Device is rooted", Toast.LENGTH_SHORT);
            toast.show();
            } else {
            debugText.setText("Root was denied!");
            }
        } catch (IOException e) {
            debugText.setText(e.getMessage());
        } catch (InterruptedException e ){
            debugText.setText(e.getMessage());
        }
    }
}