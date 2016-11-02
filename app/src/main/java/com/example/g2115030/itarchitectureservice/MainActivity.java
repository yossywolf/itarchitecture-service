package com.example.g2115030.itarchitectureservice;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button manageButton = null;
    private TextView statusText = null;
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // UI部品を初期化
        manageButton = (Button)findViewById(R.id.serviceManageButton);
        manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleService();
            }
        });
        statusText = (TextView)findViewById(R.id.statusText);
        setServiceStatusToActivity(checkServiceStatus());


        // サービスクラスを指定
        intent = new Intent(MyService.class.getName());
        intent.setPackage("com.example.g2115030.itarchitectureservice");
    }

    // サービスを接続/接続解除した時の動作
    private ServiceConnection connect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("ACTIVITY-DEBUG", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("ACTIVITY-DEBUG", "onServiceDisconnected");
        }
    };

    // サービスの起動状態をチェックする https://goo.gl/UvGZb2
    private boolean checkServiceStatus() {
        ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> listServiceInfo = am.getRunningServices(Integer.MAX_VALUE);
        boolean found = false;

        for(ActivityManager.RunningServiceInfo current : listServiceInfo) {
            if(current.service.getClassName().equals(MyService.class.getName())) {
                found = true;
                break;
            }
        }
        return found;
    }

    // UI部品を更新する
    private void setServiceStatusToActivity(boolean status) {
        if(status == true) {
            statusText.setText("Status: RUNNING");
            statusText.setTextColor(Color.GREEN);
            manageButton.setText("STOP SERVICE");
        } else {
            statusText.setText("Status: STOPPED");
            statusText.setTextColor(Color.RED);
            manageButton.setText("RUN SERVICE");
        }
    }

    // サービスの起動/終了を切り替える
    private void toggleService() {
        if (!checkServiceStatus()) {
            // サービスの起動
            bindService(intent, connect, BIND_AUTO_CREATE);
        } else {
            // サービスの停止
            unbindService(connect);
        }
        setServiceStatusToActivity(checkServiceStatus());
    }
}
