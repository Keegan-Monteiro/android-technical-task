package com.example.minimoneybox.Utils;

import android.util.Log;

/*
Class to automatically log the class name, file name and line number of place the log is being called from
 */
public class LogUtils {

    private LogUtils() {}

    private static void log (char logType, String message) {
        StackTraceElement callersStackTraceElement = Thread.currentThread().getStackTrace()[4];

        //get all the info from the stack trace
        String className = callersStackTraceElement.getClassName();
        String fileName = callersStackTraceElement.getFileName();
        int lineNumber = callersStackTraceElement.getLineNumber();
        String methodName = callersStackTraceElement.getMethodName();

        //generate the log tag and the log message
        String logTag = className.substring(className.lastIndexOf(".")+1);
        String logMessage = methodName + "(" + fileName + ":" + lineNumber + ") - " + message;

        //call the appropriate log method
        switch (logType) {
            case 'e' :
                Log.e(logTag, logMessage);
                break;
            case 'i' :
                Log.i(logTag, logMessage);
                break;
            default:
                Log.d(logTag, logMessage);
                break;
        }
    }

    public static void d (String message) {
        log('d', message);
    }

    public static void e (String message) {
        log('e', message);
    }

    public static void i (String message) {
        log('i', message);
    }
}
