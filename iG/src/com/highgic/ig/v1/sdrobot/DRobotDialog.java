package com.highgic.ig.v1.sdrobot;

import java.util.List;

import android.annotation.SuppressLint;
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

import com.highgic.ig.R;

/**
 * 用途。
 * 
 * <br>==========================
 * <br> 公司：优视科技-游戏中心
 * <br> 开发：zhouxy3@ucweb.com
 * <br> 创建时间：2013-1-4下午03:55:21
 * <br>==========================
 */

public class DRobotDialog extends Dialog {

    private Button mOkButton;
    private Button mCancelButton;

    private String mUrl;
    private int mThreadCount = 1;
    private String mSaveFilePath;


    private DRobot mDRobot;
    private LinearLayout mProgressBarLayout;
    private ProgressBar[] mProgressBarList;

    private Handler mHandler;
    private Context mContext;

    private int mComplete = 0;

    public DRobotDialog(Context context, String url, String localFilePath) {
        super(context);

        this.mContext = context;
        this.mUrl = url;
        this.mSaveFilePath = localFilePath;
        
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                int tid = b.getInt("tid");
                int size = b.getInt("size");

                mProgressBarList[tid].setProgress(mProgressBarList[tid].getProgress() + size);

                if (mProgressBarList[tid].getProgress() == mProgressBarList[tid].getMax()) {
                    mComplete++;
                    if (mComplete == mDRobot.mThreadCount) {
                        Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drobot_dialog);

        onCreateFindViews(savedInstanceState);
        onCreateSetListeners(savedInstanceState);
        onCreateInitProgressbars(savedInstanceState);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void onCreateFindViews(Bundle savedInstanceState) {
        mOkButton = (Button) findViewById(R.id.drobot_ok_button);
        mCancelButton = (Button) findViewById(R.id.drobot_cancel_button);

        mProgressBarLayout = (LinearLayout) findViewById(R.id.drobot_progressbar_layout);
    }

    protected void onCreateSetListeners(Bundle savedInstanceState) {
        mOkButton.setOnClickListener(listener);
        mCancelButton.setOnClickListener(listener);
    }

    protected void onCreateInitProgressbars(Bundle savedInstanceState) {
        mDRobot = new DRobot(mUrl, mThreadCount, mSaveFilePath, mHandler);
        if (mDRobot.mTaskInfos != null || !mDRobot.mTaskInfos.isEmpty()) {
            addProgressBars(mDRobot.mTaskInfos);
        }


     
    }



    protected OnRobotClickListener listener = new OnRobotClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.drobot_ok_button:
                if (mDRobot != null) {
                    if (mDRobot.mTaskInfos.isEmpty()) {
                        addProgressBars(mDRobot.mTaskInfos);
                    }
                    mDRobot.startDownload();
                } else {
                    Log.w("DRobot." + this.getClass().getSimpleName(), "No DownloadDialog instance!");
                    dismiss();
                }
                break;

            case R.id.drobot_cancel_button:
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


    private void addProgressBars(List<DRobot.TaskInfo> list) {
        mProgressBarList = new ProgressBar[mDRobot.mThreadCount];
        ProgressBar bar = null;

        for (DRobot.TaskInfo info : list) {
            bar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 10);
            params.topMargin = 10;
            bar.setLayoutParams(params);
            bar.setMax(info.end - info.start);
            bar.setProgress(info.current);
            mProgressBarList[info.tid] = bar;
            mProgressBarLayout.addView(bar);
        }
    }


    private void processFinish() {
        if (mProgressBarList != null) {
            for (ProgressBar bar : mProgressBarList) {
                mProgressBarLayout.removeView(bar);
            }
        }
    }
}
