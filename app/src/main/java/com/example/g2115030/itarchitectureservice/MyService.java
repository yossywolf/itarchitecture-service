package com.example.g2115030.itarchitectureservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    private int scale = 0;
    private int level = 0;
    private String batteryString = "Not Initialized";

    // https://goo.gl/QxKgfs
    static final String BR = System.getProperty("line.separator");

    // クライアントから送られた文字列を送り返す
    // ……バッテリーの容量を添えて
    private IMyAidlInterface.Stub mStub = new IMyAidlInterface.Stub() {
        @Override
        public String echoFromService(String message) {
            Log.d("SERVICE", message);
            String echoString = message + BR + batteryString + String.format(" - %d/%d", level, scale);
            return echoString;
        }

    };

    @Override
    public void onCreate() {
        Log.d("SERVICE-DEBUG", "onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SERVICE-DEBUG", "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("SERVICE-DEBUG", "onBind");

        // 電池残量のブロードキャスト・インテントを取得する
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(myBatteryReceiver, filter);

        return mStub;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("SERVICE-DEBUG", "onUnbind");

        unregisterReceiver(myBatteryReceiver);

        return super.onUnbind(intent);
    }

    public BroadcastReceiver myBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                scale = intent.getIntExtra("scale", 0);
                level = intent.getIntExtra("level", 0);

                // 一度でもバッテリーの容量を取得できたら"Not Initialized"は消す
                batteryString = "バッテリー残量";
                String batteryToast = batteryString + String.format(" - level: %d / scale: %d", level, scale);
                Toast.makeText(context, batteryToast, Toast.LENGTH_SHORT).show();
            }
        }
    };

}
