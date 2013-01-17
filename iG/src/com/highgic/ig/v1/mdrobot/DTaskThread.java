package com.highgic.ig.v1.mdrobot;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 下载任务线程。
 *
 * <br>==========================
 * <br> 公司：优视科技-游戏中心
 * <br> 开发：zhouxy3@ucweb.com
 * <br> 创建时间：2013-1-4上午10:25:04
 * <br>==========================
 */

public class DTaskThread extends Thread {
    private DTaskInfo mDTaskInfo;
    private Handler mHandler;
    private Dao mDao;
    

    public DTaskThread(Dao dao, DTaskInfo taskInfo) {
        this.mDTaskInfo = taskInfo;
        this.mDao = dao;
    }


    public void run() {

        try {
            URL u = new URL(mDTaskInfo.url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setConnectTimeout(5000);

            conn.setRequestProperty("Range", "bytes=" + (mDTaskInfo.start + mDTaskInfo.current) + "-" + mDTaskInfo.end);
            RandomAccessFile raf = new RandomAccessFile(new File(mDTaskInfo.localPathName), "rwd");

            raf.seek(mDTaskInfo.start + mDTaskInfo.current);

            InputStream is = conn.getInputStream();

            byte[] buffer = new byte[10240];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                raf.write(buffer, 0, len);


                mDao.updateTaskInfo(mDTaskInfo.tid, len);

                Message msg = new Message();
                Bundle b = new Bundle();
                b.putInt("tid", mDTaskInfo.tid);
                b.putInt("fid", mDTaskInfo.fid);
                b.putInt("size", len);
                msg.setData(b);
                mHandler.sendMessage(msg);
            }
            is.close();
            raf.close();
        } catch (Exception e) {

            Log.w("DownloadRobot." + this.getClass().getSimpleName(), "tid = " + mDTaskInfo.tid + e.getMessage());
        }

    }
}