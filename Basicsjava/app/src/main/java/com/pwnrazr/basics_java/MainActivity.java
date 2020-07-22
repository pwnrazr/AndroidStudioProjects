package com.pwnrazr.basics_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static void method(String test) {
        Log.i("basic-log", "method call");
        Log.i("basic-log",test);
    }

    static String stringMethod() {
        int number = 128;
        return "return string here";
    }

    public class myClass {
        int number = 1998;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView debugText = findViewById(R.id.debugText);
        debugText.setText("Hello");

        method("arg method here");
        Log.i("basic-log",stringMethod());

        myClass mycls = new myClass();
        String store = Integer.toString(mycls.number);
        Log.i("basic-log","birth year: " + store);
    }
}