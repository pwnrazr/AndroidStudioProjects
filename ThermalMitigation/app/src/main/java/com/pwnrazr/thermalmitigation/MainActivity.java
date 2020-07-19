package com.pwnrazr.thermalmitigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView debugText = findViewById(R.id.debugText);
        final TextView statusText = findViewById(R.id.statusText);

        Switch thermSwitch = findViewById(R.id.thermSwitch);

        try {
            Process cmd;
            //cmd = Runtime.getRuntime().exec("su -c mount -o rw,remount / && echo tested > /system/testenment && rm /system/testenment && mount -o ro,remount /");
            cmd = Runtime.getRuntime().exec("su -c cat /sys/module/smb5_lib/parameters/skip_therm");
                    cmd.waitFor();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(cmd.getInputStream()));

            // Get current skip thermal state
            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {    //append chars from InputStream
                log.append(line + "\n");
            }
            if(log.toString().equals("1\n")) {
                statusText.setText("Enabled");
                thermSwitch.setChecked(true);
            }else if(log.toString().equals("0\n")){
                statusText.setText("Disabled");
                thermSwitch.setChecked(false);
            }else {
                statusText.setText("ERROR");
            }
            //debugText.setText(Integer.toString(cmd.exitValue()));
        } catch (IOException | InterruptedException e) {
            debugText.setText(e.getMessage());
        }

        thermSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //debugText.setText("Enabled!");
                    try {
                        Process cmd_modify;
                        cmd_modify = Runtime.getRuntime().exec("su -c echo 1 > /sys/module/smb5_lib/parameters/skip_therm");
                        cmd_modify.waitFor();
                    } catch (IOException | InterruptedException e) {
                        debugText.setText(e.getMessage());
                    }
                    //verify that it is set
                    try {
                        Process cmd;

                        cmd = Runtime.getRuntime().exec("su -c cat /sys/module/smb5_lib/parameters/skip_therm");
                        cmd.waitFor();
                        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(cmd.getInputStream()));

                        // Get current skip thermal state
                        StringBuilder log = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {    //append chars from InputStream
                            log.append(line + "\n");
                        }
                        if(log.toString().equals("1\n")) {
                            statusText.setText("Enabled");
                        }else if(log.toString().equals("0\n")){
                            statusText.setText("Disabled");
                        }else {
                            statusText.setText("ERROR");
                        }
                    } catch (IOException | InterruptedException e) {
                        debugText.setText(e.getMessage());
                    }

                } else {
                    //debugText.setText("Disabled");
                    try {
                        Process cmd_modify;
                        cmd_modify = Runtime.getRuntime().exec("su -c echo 0 > /sys/module/smb5_lib/parameters/skip_therm");
                        cmd_modify.waitFor();
                    } catch (IOException | InterruptedException e) {
                        debugText.setText(e.getMessage());
                    }
                    //verify that it is set
                    try {
                        Process cmd;

                        cmd = Runtime.getRuntime().exec("su -c cat /sys/module/smb5_lib/parameters/skip_therm");
                        cmd.waitFor();
                        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(cmd.getInputStream()));

                        // Get current skip thermal state
                        StringBuilder log = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {    //append chars from InputStream
                            log.append(line + "\n");
                        }
                        if(log.toString().equals("1\n")) {
                            statusText.setText("Enabled");
                        }else if(log.toString().equals("0\n")){
                            statusText.setText("Disabled");
                        }else {
                            statusText.setText("ERROR");
                        }
                    } catch (IOException | InterruptedException e) {
                        debugText.setText(e.getMessage());
                    }
                }
            }
        });
    }
}