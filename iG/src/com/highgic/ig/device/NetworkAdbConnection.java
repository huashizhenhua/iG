/*
 * Copyright (C) 2013 AdbWifiConnection.java authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.highgic.ig.device;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.highgic.ig.R;

/**
 * @author zhouxiaoyi
 * 
 */
public class NetworkAdbConnection extends Activity {

    public static final String TAG = "NetworkAdbConnection";


    private Button enableButton;
    private Button disableButton;
    private TextView resultTextView;
    private TextView cmdTextView;


    private ProgressDialog pd;
    
   

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adb_wifi_connection);
        
        pd = new ProgressDialog(NetworkAdbConnection.this);
        pd.setMessage("正在执行操作！");

        resultTextView = (TextView) findViewById(R.id.network_adb_result_textview);

        cmdTextView = (TextView) findViewById(R.id.network_adb_cmd_textview);
        cmdTextView.setText("adb connect " + getLocalIpAddress() + ":8787");


        disableButton = (Button) findViewById(R.id.network_adb_disable_button);
        enableButton = (Button) findViewById(R.id.network_adb_enable_button);



        enableButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFeature(true);
            }
        });

        disableButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFeature(false);
            }
        });

    }


    /**
     * @return
     */
    private void enableFeature(final boolean isEnable) {
        Thread  t;
        t = new Thread(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, "start");

                mHandler.sendEmptyMessage(1);

                if (isEnable) {
                    RootCommand.command("setprop service.adb.tcp.port 8787");
                } else {
                    RootCommand.command("setprop service.adb.tcp.port -1");
                }

                RootCommand.command("stop adbd");
                RootCommand.command("start adbd");

                Log.d(TAG, "stop");


                mHandler.sendEmptyMessage(2);

            }
        });

        t.start();
    }


    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return "";
    }


    Handler mHandler = new Handler(new Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
            case 1:
                pd.show();
                break;
            case 2:
                pd.dismiss();
                break;
            }

            return false;
        }
    });

}
