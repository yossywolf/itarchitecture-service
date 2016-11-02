package com.example.g2115030.itarchitectureservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class MyService extends Service {
    private IMyAidlInterface.Stub mStub = new IMyAidlInterface.Stub() {
        @Override
        public void echoFromService(String message) {
            Log.d("SERVICE", message);
        }

    };

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
        private int scale;
        private int level;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                scale = intent.getIntExtra("scale", 0);
                level = intent.getIntExtra("level", 0);

                String batteryToast = String.format("バッテリー残量が変化しました - level: %d / scale: %d", level, scale);
                Toast.makeText(context, batteryToast, Toast.LENGTH_SHORT).show();
            }
        }
    };

}
