package com.highgic.ig.update;
/*package com.highgic.ig.v1.demo.update;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.highgic.ig.R;



*//**
 * MainActivity
 *//*
public class DRobotActivity extends Activity implements OnClickListener {


    private static String url = "http://wap3.ucweb.com/files/UCBrowser/zh-cn/999/UCBrowser_V8.7.4.225_Android_pf145_(Build12121509).apk";
    private static int count = 3;
    private static String localFilePath = "/sdcard/UCBrowser_V8.7.4.225_Android_pf145_(Build12121509).apk";


    private DRobot downloadRobot;

    private LinearLayout rootLinearlayout;
    private ProgressBar[] bars;
    private int completeCount = 0;



    private DRobotDialog dialog;

    private Handler h = new Handler() {
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            int index = b.getInt("tid");
            int size = b.getInt("size");
            bars[index].setProgress(bars[index].getProgress() + size);
            if (bars[index].getProgress() == bars[index].getMax()) {
                completeCount++;
                if (completeCount == downloadRobot.taskCount) {
                    Toast.makeText(DRobotActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    processFinish();
                }
            }
        }
    };

    private void processFinish() {
        if (bars != null) {
            for (ProgressBar bar : bars) {
                rootLinearlayout.removeView(bar);
            }
        }

        downloadRobot.clearTaskInfo();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLinearlayout = (LinearLayout) findViewById(R.id.rootLinearlayout);

        findViewById(R.id.startButton).setOnClickListener(this);
        findViewById(R.id.stopButton).setOnClickListener(this);
        findViewById(R.id.clearButton).setOnClickListener(this);
        findViewById(R.id.dialogButton).setOnClickListener(this);


    }

    public void onClick(View v) {
        int id = v.getId();

        downloadRobot = new DRobot(url, count, localFilePath, h, this);

        if (id == R.id.startButton) {

            v.post(new Runnable() {

                @Override
                public void run() {
                    addProgressBars(downloadRobot.taskInfos);
                    downloadRobot.startDownload();
                }
            });

        } else if (id == R.id.clearButton) {
            processFinish();
        } else if (id == R.id.stopButton) {

            //            downloadRobot.stopDownload();

        } else if (id == R.id.dialogButton) {


            v.post(new Runnable() {
                @Override
                public void run() {
                   dialog = new DRobotDialog(DRobotActivity.this);
                   dialog.show();
                }
            });


        }
    }

    private void addProgressBars(List<DRobot.TaskInfo> list) {
        bars = new ProgressBar[downloadRobot.taskCount];
        ProgressBar bar = null;
        for (DRobot.TaskInfo info : list) {
            bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 3);
            params.topMargin = 10;
            bar.setLayoutParams(params);
            bar.setMax(info.end - info.start);
            bar.setProgress(info.current);
            bars[info.tid] = bar;
            rootLinearlayout.addView(bar);
        }



    }
}*/