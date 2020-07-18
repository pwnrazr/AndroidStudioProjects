package com.pwnrazr.roottest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView debugText = (TextView) findViewById(R.id.debugText);
        debugText.setText("No error");
        Process p;
        try {
            p = Runtime.getRuntime().exec("su -c mount -o rw,remount / && echo tested > /system/testenment && mount -o ro,remount /");
            p.waitFor();
            debugText.setText(Integer.toString(p.exitValue()));
        } catch (IOException e) {
            debugText.setText(e.getMessage());
        } catch (InterruptedException e ){
            debugText.setText(e.getMessage());
        }
    }
}