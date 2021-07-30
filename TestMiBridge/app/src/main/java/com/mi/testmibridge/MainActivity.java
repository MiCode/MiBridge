package com.mi.testmibridge;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.mi.mibridge.DeviceLevel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        int cpu = DeviceLevel.getDeviceLevel(1, DeviceLevel.CPU);
        int gpu = DeviceLevel.getDeviceLevel(1, DeviceLevel.GPU);
        int ram = DeviceLevel.getDeviceLevel(1, DeviceLevel.RAM);

        TextView cpuTv = findViewById(R.id.cpu_level);
        TextView gpuTv = findViewById(R.id.gpu_level);
        TextView ramTv = findViewById(R.id.ram_level);
        TextView liteTv = findViewById(R.id.is_miui_lite);

        cpuTv.setText(getLevelName(cpu,"CPU level: "));
        gpuTv.setText(getLevelName(gpu,"GPU level: "));
        ramTv.setText(getLevelName(ram,"RAM level: "));
        liteTv.setText(DeviceLevel.IS_MIUI_LITE_VERSION? "当前系统是Miui Lite":"当前系统不是Miui Lite");

        findViewById(R.id.more_api_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ApiActivity.class);
                startActivity(intent);
            }
        });
        Log.d(TAG, "cpu is " + cpu + " gpu is " + gpu + " ram is " + ram);
    }

    private String getLevelName(int level, String prev) {
        String levelType;
        if (level == DeviceLevel.LOW) {
            levelType = "Low";
        } else if (level == DeviceLevel.MIDDLE) {
            levelType = "Middle";
        } else if (level == DeviceLevel.HIGH) {
            levelType = "High";
        } else {
            levelType = " Unknown";
        }
        return prev + levelType;
    }
}
