package com.imob.weblogcat.demo;

import android.util.Log;

import com.imob.weblogcat.WebLogCat;

public class MyLog {

    public static void log(String tag, String msg, Throwable throwable, int logLevel) {

        WebLogCat.log(tag, msg, logLevel, throwable);

        switch (logLevel) {
            case Log.VERBOSE:
                Log.v(tag, msg, throwable);
                break;
            case Log.DEBUG:
                Log.d(tag, msg, throwable);
                break;
            case Log.INFO:
                Log.i(tag, msg, throwable);
                break;
            case Log.WARN:
                Log.w(tag, msg, throwable);
                break;
            case Log.ERROR:
                Log.e(tag, msg, throwable);
                break;
        }
    }
}
