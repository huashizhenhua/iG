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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
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
public class AdbWifiConnection extends Activity {

    private Button execButton;
    private TextView resultTextView;
    private TextView pcShellTextView;

    private StringBuilder sb;

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adb_wifi_connection);

        resultTextView = (TextView) findViewById(R.id.textview_shell_result);
        pcShellTextView = (TextView) findViewById(R.id.textview_exec_pc_shell);

        pcShellTextView.setText("adb connect 192.168.2.255:5555");

        sb = new StringBuilder();

        execButton = (Button) findViewById(R.id.button_exec_shell);
        final Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                Log.d("AdbWifiConnection", "start");
                sb.append(execShellStr("su"));
                Log.d("AdbWifiConnection", sb.toString());
                sb.append(execShellStr(execShellStr("setprop service.adb.tcp.port 5555")));
                Log.d("AdbWifiConnection", sb.toString());
                sb.append(execShellStr(execShellStr("stop adbd")));
                Log.d("AdbWifiConnection", sb.toString());
                sb.append(execShellStr(execShellStr("start adbd")));
                Log.d("AdbWifiConnection", sb.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultTextView.setText(sb.toString());
                    }
                });
                Log.d("AdbWifiConnection", "stop");
            }
        });



        execButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                t.start();

            }
        });

    }


    /** 执行 shell 命令之后返回 String 类型的结果 */
    public static String execShellStr(String cmd) {
        String[] cmdStrings = new String[] { "sh", "-c", cmd };
        String retString = "";

        try {
            Process process = Runtime.getRuntime().exec(cmdStrings);
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()), 7777);
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()), 7777);

            String line = null;

            while ((null != (line = stdout.readLine())) || (null != (line = stderr.readLine()))) {
                if (false == isStringEmpty(line)) {
                    retString += line + "\n";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return retString;
    }


    /**
     * @param line
     * @return
     */
    private static boolean isStringEmpty(String line) {
        boolean value = false;
        if (line != null && !line.equals("")) {
            value = false;
        } else {
            value = true;
        }
        return value;
    }


}
