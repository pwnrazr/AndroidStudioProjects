package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    Button writebtn;
    Button readbtn;
    Button setbtn;

    FileWriter stream;
    FileReader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, 1);     //prompt for storage permissions

        writebtn = (Button) findViewById(R.id.writebutton);
        readbtn = (Button) findViewById(R.id.readbutton);
        setbtn = (Button) findViewById(R.id.setbutton);

        final EditText textIn = (EditText) findViewById(R.id.textinput);
        final TextView readText = (TextView) findViewById(R.id.readtext);
        TextView pathText = (TextView) findViewById(R.id.pathtext);
        final TextView debugText = (TextView) findViewById(R.id.debugText);

        debugText.setText("No error");

        final char[] charread = new char[99];

        //file setup
        File path = getFilesDir();
        final File file = new File(path, "testenment.txt");

        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            debugText.setText(e.toString());
        }

        pathText.setText("Path:" + path.toString());  //set my textbox to display the path thing

        writebtn.setOnClickListener(new View.OnClickListener() {    //write button
            @Override
            public void onClick(View view) {
                //readText.setText("Write button"); //for debug
                try {
                    stream = new FileWriter(file);
                } catch (IOException e) {
                    //e.printStackTrace();
                    debugText.setText(e.toString());
                }
                try {
                    stream.write(textIn.getText().toString());
                    stream.flush();
                } catch (IOException e) {
                    debugText.setText(e.toString());
                }
            }
        });

        readbtn.setOnClickListener(new View.OnClickListener() {    //read button
            @Override
            public void onClick(View view) {
                //readText.setText("Read Button");  //for debug
                try {
                    reader.read(charread);
                } catch (IOException e) {
                    debugText.setText(e.toString());
                }

                readText.setText(new String(charread));
            }
        });

        setbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                debugText.setText("that button does nothing");    //set
            }
        });
    }
}
