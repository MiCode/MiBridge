package com.mi.testmibridge;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bun.miitmdid.core.JLibrary;
import com.mi.mibridge.MiBridge;

public class MainActivity extends AppCompatActivity {

    private Button bt_check;
    private Button bt_check_debug;
    private Button bt_getvaid;
    private Button bt_more;

    private TextView text_check_ret;
    private TextView text_debug_ret;
    private TextView text_vaid;

    private EditText auth_key;

    //VAID
    private static String my_vaid;

    private MiitHelper.AppIdsUpdater appIdsUpdater = new MiitHelper.AppIdsUpdater() {
        @Override
        public void OnIdsAvalid(@NonNull String ids) {
            my_vaid = ids;
        }
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        JLibrary.InitEntry(base);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取VAID等设备标识符
        MiitHelper miitHelper = new MiitHelper(appIdsUpdater);
        miitHelper.getDeviceIds(getApplicationContext());

        //CheckPermission
        bt_check = (Button) findViewById(R.id.btn_permission);
        text_check_ret = (TextView) findViewById(R.id.text_check_result);

        bt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ret = false;
                ret = MiBridge.checkPermission("com.mi.testmibridge",android.os.Process.myUid());

                if (ret)
                    text_check_ret.setText("Success");
                else
                    text_check_ret.setText("Fail");
            }
        });

        //CheckDebugPermission
        bt_check_debug = (Button) findViewById(R.id.btn_debug);
        text_debug_ret = (TextView) findViewById(R.id.text_check_debug_result);
        auth_key = (EditText) findViewById(R.id.editText_authkey);

        bt_check_debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String authKey = auth_key.getText().toString();
                if (!"".equals(authKey)) {
                    boolean ret = false;
                    ret = MiBridge.checkDebugPermission(MainActivity.this,"com.mi.testmibridge", android.os.Process.myUid(), authKey);

                    if (ret)
                        text_debug_ret.setText("Success");
                    else
                        text_debug_ret.setText("Fail");
                } else {
                    text_debug_ret.setText("Please input authentication key!");
                }
            }
        });

        //Test Get VAID
        bt_getvaid = (Button) findViewById(R.id.btn_vaid);
        text_vaid = (TextView) findViewById(R.id.text_vaid);

        bt_getvaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_vaid.setText(my_vaid);
            }
        });

        //Test More Performance APIs (request cpu ...)
        bt_more = (Button) findViewById(R.id.btn_more);

        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PerformanceAPIs.class);
                startActivity(intent);
            }
        });

    }
}
