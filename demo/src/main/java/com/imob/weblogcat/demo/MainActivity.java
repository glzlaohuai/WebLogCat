package com.imob.weblogcat.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.imob.weblogcat.WebLogCat;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyCustomLogTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebLogCat.init(this);
    }

    public void printLogs(View view) {
        MyLog.log(TAG, "hello world, java", new RuntimeException("wtf"), Log.VERBOSE);
        MyLog.log(TAG, "hello world, python", new RuntimeException("wtf"), Log.DEBUG);
        MyLog.log(TAG, "hello world javascript", new RuntimeException("wtf"), Log.INFO);
        MyLog.log(TAG, "hello world php", new RuntimeException("wtf"), Log.WARN);
        MyLog.log(TAG, "hello world swift", new RuntimeException("wtf"), Log.ERROR);
    }
}