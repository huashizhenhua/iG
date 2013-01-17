package com.highgic.ig.v1.sdrobot;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 下载工具类。
 *
 * <br>==========================
 * <br> 公司：优视科技-游戏中心
 * <br> 开发：zhouxy3@ucweb.com
 * <br> 创建时间：2013-1-4下午03:51:14
 * <br>==========================
 */

public class DRobot {
    private String mUrl;
    public int mThreadCount;
    public String mSaveFilePathAndName;
    public int mFileLength;
    public Handler h;
    public List<TaskInfo> mTaskInfos = new ArrayList<TaskInfo>();
    public List<DownloadTask> mDownloadTasks = new ArrayList<DownloadTask>();

    public DRobot(String url, int count, String saveFilePathAndName, Handler handler) {
        this.mUrl = url;
        this.mThreadCount = count;
        this.mSaveFilePathAndName = saveFilePathAndName;
        this.h = handler;
        initDownloadRobot();
    }

    /**
     * 启动下载
     */
    public void startDownload() {
        
        
        for (TaskInfo info : mTaskInfos) {
            DownloadTask task = new DownloadTask(info.tid, info.start, info.end, info.current);
            mDownloadTasks.add(task);
            task.start();
        }
        
        Log.d("DRobot." + this.getClass().getSimpleName(), "startDownload..." + mTaskInfos.toString());
        
    }




    private void initDownloadRobot() {
            try {
                URL u = new URL(mUrl);
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setConnectTimeout(5000);
                mFileLength = conn.getContentLength();
                File f = new File(mSaveFilePathAndName);
                RandomAccessFile raf = new RandomAccessFile(f, "rwd");
                raf.setLength(mFileLength);
            } catch (Exception e) {
                Log.w("DRobot." + this.getClass().getSimpleName(), e.getMessage());
            }
            getTaskInfos();
    }

    private void getTaskInfos() {
        TaskInfo info = null;
        int block = mFileLength / mThreadCount;

        int start;
        int end;
        for (int i = 0; i < mThreadCount; i++) {
            start = i * block;
            if (i != (mThreadCount - 1)) {
                end = (i + 1) * block - 1;
            } else {
                end = mFileLength - 1;
            }
            info = new TaskInfo();
            info.start = start;
            info.end = end;
            info.tid = i;
            mTaskInfos.add(info);
        }
    }
    



    /**
     * 下载任务信息实体类
     * 
     * @author zhouxy3
     * 
     */

    public class TaskInfo {
        public int start;
        public int end;
        public int current;
        public int tid;
    }

    /**
     * 下载任务线程
     * 
     * @author zhouxy3
     * 
     */
    public class DownloadTask extends Thread {
        public int start;
        public int end;
        public int tid;
        public int current;


        public DownloadTask(int tid, int startPos, int endPos, int amount) {
            this.tid = tid;
            this.start = startPos;
            this.end = endPos;
            this.current = amount;
        }


        public void run() {

            try {
                URL u = new URL(mUrl);
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setConnectTimeout(5000);

                conn.setRequestProperty("Range", "bytes=" + (start + current) + "-" + end);
                RandomAccessFile raf = new RandomAccessFile(new File(mSaveFilePathAndName), "rwd");

                raf.seek(start + current);

                InputStream is = conn.getInputStream();

                byte[] buffer = new byte[10240];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    raf.write(buffer, 0, len);


                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putInt("tid", tid);
                    b.putInt("size", len);
                    msg.setData(b);
                    h.sendMessage(msg);
                }
                is.close();
            } catch (Exception e) {
//                Log.w("DRobot." + this.getClass().getSimpleName(), e.getMessage());
            }

        }
    }

}

