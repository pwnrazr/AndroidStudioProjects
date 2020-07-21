package com.pwnrazr.iotcontrolpoc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String statusmsg = "";
    String[] statusmsgSeparated;
    boolean updateReady = false;

    private class Background_get extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://192.168.1.179/?" + params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                Log.i("pwnrazr",Integer.toString(connection.getResponseCode()));
                Log.i("pwnrazr",connection.getResponseMessage());
                statusmsg = connection.getResponseMessage();
                statusmsgSeparated = statusmsg.split(",");  //splits statusmsg with comma(,) as separator
                updateReady = true;

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    result.append(inputLine).append("\n");

                in.close();
                connection.disconnect();
                return result.toString();
            } catch (IOException e) {
                Log.e("pwnrazr",e.toString());
            }

            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button testButton = findViewById(R.id.testButton);
        Button offButton = findViewById(R.id.offButton);
        final ToggleButton toggleButton = findViewById(R.id.toggleButton);

        new Background_get().execute("reqled");     //Request status of LED on app startup

        new CountDownTimer(100, 100)  //loop update
        {
            public void onTick(long l) {}
            public void onFinish()
            {
                if(updateReady) {   //Only update when there's actually something to update
                    TextView debugText = findViewById(R.id.debugText);
                    //debugText.setText(statusmsg);
                    debugText.setText(String.format("%s=test=%s", statusmsgSeparated[0], statusmsgSeparated[1]));
                    if(statusmsgSeparated[1].equals("ON")){
                        toggleButton.setChecked(true);
                    } else {
                        toggleButton.setChecked(false);
                    }
                    updateReady = false;
                }

                start();
            }
        }.start();

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Background_get().execute("led1=1");
            }
        });

        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Background_get().execute("led1=0");
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    new Background_get().execute("led1=1");
                } else {
                    new Background_get().execute("led1=0");
                }
            }
        });
    }
}

