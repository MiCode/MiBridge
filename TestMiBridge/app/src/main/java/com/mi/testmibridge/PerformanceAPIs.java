package com.mi.testmibridge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mi.mibridge.MiBridge;

public class PerformanceAPIs extends AppCompatActivity {

    private Button bt_requestCpu;
    private Button bt_cancelCpu;
    private Button bt_requestThread;
    private Button bt_cancelThread;

    private TextView text_requestCpu_ret;
    private TextView text_cancelCpu_ret;
    private TextView text_requestThread_ret;
    private TextView text_cancelThread_ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_apis);

        //requestCpuHighFreq
        bt_requestCpu = (Button) findViewById(R.id.btn_requestcpu);
        text_requestCpu_ret = (TextView) findViewById(R.id.text_reqcpu_ret);

        bt_requestCpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ret = 0;
                ret = MiBridge.requestCpuHighFreq(android.os.Process.myUid(), 1,10000);

                if (ret == 0)
                    text_requestCpu_ret.setText("Success");
                else if (ret == -2)
                    text_requestCpu_ret.setText("No Permission");
                else
                    text_requestCpu_ret.setText("Fail");
            }
        });

        //cancelCpuHighFreq
        bt_cancelCpu = (Button) findViewById(R.id.btn_cancelcpu);
        text_cancelCpu_ret = (TextView) findViewById(R.id.text_cancelcpu_ret);

        bt_cancelCpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ret = 0;
                ret = MiBridge.cancelCpuHighFreq(android.os.Process.myUid());

                if (ret == 0)
                    text_cancelCpu_ret.setText("Success");
                else if (ret == -2)
                    text_cancelCpu_ret.setText("No Permission");
                else
                    text_cancelCpu_ret.setText("Fail");
            }
        });

        //requestThreadPriority
        bt_requestThread = (Button) findViewById(R.id.btn_requestthread);
        text_requestThread_ret = (TextView) findViewById(R.id.text_reqthread_ret);

        bt_requestThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ret = 0;
                ret = MiBridge.requestThreadPriority(android.os.Process.myUid(), android.os.Process.myTid(),10000);

                if (ret == 0)
                    text_requestThread_ret.setText("Success");
                else if (ret == -2)
                    text_requestThread_ret.setText("No Permission");
                else
                    text_requestThread_ret.setText("Fail");
            }
        });

        //cancelThreadPriority
        bt_cancelThread = (Button) findViewById(R.id.btn_cancelthread);
        text_cancelThread_ret = (TextView) findViewById(R.id.text_cancelthread_ret);

        bt_cancelThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ret = 0;
                ret = MiBridge.cancelThreadPriority(android.os.Process.myUid(), android.os.Process.myTid());

                if (ret == 0)
                    text_cancelThread_ret.setText("Success");
                else if (ret == -2)
                    text_cancelThread_ret.setText("No Permission");
                else
                    text_cancelThread_ret.setText("Fail");
            }
        });
    }
}
