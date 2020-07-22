package com.pwnrazr.thermalmitigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    static String root_cmd(String command) {
        StringBuilder log = new StringBuilder();
        try {
            Process cmd;
            cmd = Runtime.getRuntime().exec(command);
            cmd.waitFor();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(cmd.getInputStream()));

            // Get output from command
            String line;
            while ((line = bufferedReader.readLine()) != null) {    // Append chars from InputStream
                log.append(line + "\n");
            }
        } catch (IOException | InterruptedException e) {
            Log.e("ThermMitigationApp",e.toString());
        }
        return log.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView debugText = findViewById(R.id.debugText);
        final TextView statusText = findViewById(R.id.statusText);

        Switch thermSwitch = findViewById(R.id.thermSwitch);

        String returnCMD = "";
        // Get current skip thermal state on app startup
        returnCMD = root_cmd("su -c cat /sys/module/smb5_lib/parameters/skip_therm");

        if(returnCMD.equals("1\n")) {
            statusText.setText("Enabled");
            thermSwitch.setChecked(true);
        }else if(returnCMD.equals("0\n")){
            statusText.setText("Disabled");
            thermSwitch.setChecked(false);
        }else {
            statusText.setText("ERROR");
        }

        thermSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //debugText.setText("Enabled!");
                    root_cmd("su -c echo 1 > /sys/module/smb5_lib/parameters/skip_therm");

                    //verify that it is set
                    String returnCMD = "";
                    returnCMD = root_cmd("su -c cat /sys/module/smb5_lib/parameters/skip_therm");

                    if(returnCMD.equals("1\n")) {
                        statusText.setText("Enabled");
                    }else if(returnCMD.equals("0\n")){
                        statusText.setText("Disabled");
                    }else {
                        statusText.setText("ERROR");
                    }

                } else {
                    //debugText.setText("Disabled");
                    root_cmd("su -c echo 0 > /sys/module/smb5_lib/parameters/skip_therm");

                    //verify that it is set
                    String returnCMD = "";
                    returnCMD = root_cmd("su -c cat /sys/module/smb5_lib/parameters/skip_therm");

                    if(returnCMD.equals("1\n")) {
                        statusText.setText("Enabled");
                    }else if(returnCMD.equals("0\n")){
                        statusText.setText("Disabled");
                    }else {
                        statusText.setText("ERROR");
                    }
                }
            }
        });
    }
}