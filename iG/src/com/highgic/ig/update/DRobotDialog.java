package com.highgic.ig.update;

import java.util.List;

import com.highgic.ig.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;



public class DRobotDialog extends Dialog {

    private Button okButton;
    private Button cancelButton;

    private static String url = "http://img.baidu.com/img/iknow/wenku/BaiduWenku_Android_2-4-0-0_10000.apk";
    private static int count = 1;
    private static String localFilePath = "/sdcard/BaiduWenku_Android_2-4-0-0_10000.apk";


    private DRobot dRobot;
    private LinearLayout barsLayout;
    private ProgressBar[] bars;
    private int completeCount = 0;

    private Context context;



    public DRobotDialog(Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_robot);

        onCreateFindViews(savedInstanceState);
        onCreateSetListeners(savedInstanceState);


    }



    @Override
    protected void onStart() {
        super.onStart();
        onCreateInitProgressbars(null);

    }


    protected void onCreateFindViews(Bundle savedInstanceState) {
        okButton = (Button) findViewById(R.id.ok_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);

        barsLayout = (LinearLayout) findViewById(R.id.progressBarLayout);
    }

    protected void onCreateSetListeners(Bundle savedInstanceState) {
        okButton.setOnClickListener(listener);
        cancelButton.setOnClickListener(listener);
    }

    protected OnRobotClickListener listener = new OnRobotClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.ok_button:
                if (dRobot != null) {
                    if (dRobot.taskInfos.isEmpty()) {
                        addProgressBars(dRobot.taskInfos);
                    }
                    dRobot.startDownload();
                } else {
                    Log.w("DownloadRobot." + this.getClass().getSimpleName(), "No DownloadDialog instance!");
                    dismiss();
                }
                break;

            case R.id.cancel_button:
                processFinish();
                dismiss();
                break;
            }
        }
    };


    private class OnRobotClickListener implements android.view.View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }

    protected void onCreateInitProgressbars(Bundle savedInstanceState) {

        dRobot = new DRobot(url, count, localFilePath, h, context);
        if (dRobot.taskInfos != null || !dRobot.taskInfos.isEmpty()) {
            addProgressBars(dRobot.taskInfos);
        }
    }


    private void addProgressBars(List<DRobot.TaskInfo> list) {
        bars = new ProgressBar[dRobot.taskCount];
        ProgressBar bar = null;

        for (DRobot.TaskInfo info : list) {
            bar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 10);
            params.topMargin = 10;
            bar.setLayoutParams(params);
            bar.setMax(info.end - info.start);
            bar.setProgress(info.current);
            bars[info.tid] = bar;
            barsLayout.addView(bar);
        }
    }


    private Handler h = new Handler() {
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            int tid = b.getInt("tid");
            int size = b.getInt("size");

            bars[tid].setProgress(bars[tid].getProgress() + size);

            if (bars[tid].getProgress() == bars[tid].getMax()) {
                completeCount++;
                if (completeCount == dRobot.taskCount) {
                    Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                    processFinish();
                    dRobot.clearTaskInfo();
                }
            }
        }
    };

    private void processFinish() {
        if (bars != null) {
            for (ProgressBar bar : bars) {
                barsLayout.removeView(bar);
            }
        }
    }
}
