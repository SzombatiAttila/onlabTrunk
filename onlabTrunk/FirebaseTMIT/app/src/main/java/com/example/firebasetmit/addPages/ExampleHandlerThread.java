package com.example.firebasetmit.addPages;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ExampleHandlerThread extends HandlerThread {
    public static final String TAG = "ExampleHandlerThread";
    public static final int EXAMPLE_TASK = 1;

    private Handler mHandler;

    public ExampleHandlerThread(){
        super("ExampleHandlerThread");
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared(){
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case EXAMPLE_TASK:
                        Log.d(TAG, "handleMessage: Debug task : arg1 : " + msg.arg1 + ", " +
                                "obj : " + msg.obj);
                        break;
                    default:
                        super.handleMessage(msg);
                }

            }
        };
    }

    public Handler getHandler(){
        return mHandler;
    }
}
