package com.example.g2115030.itarchitectureservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button manageButton = null;
    private TextView statusText = null;
    private Intent intent = null;
    private boolean mode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manageButton = (Button)findViewById(R.id.serviceManageButton);
        manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleService();
            }
        });

        statusText = (TextView)findViewById(R.id.statusText);


        // サービスクラスを指定
        intent = new Intent(MyService.class.getName());

        intent.setPackage("com.example.g2115030.itarchitectureservice");
    }

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

    private void toggleService() {
        if (!mode) {
            // サービスの起動
            bindService(intent, connect, BIND_AUTO_CREATE);
            statusText.setText("Status: RUNNING");
            manageButton.setText("STOP SERVICE");
            mode = true;
        } else {
            // サービスの停止
            unbindService(connect);
            statusText.setText("Status: STOPPED");
            manageButton.setText("RUN SERVICE");
            mode = false;
        }
    }

}
