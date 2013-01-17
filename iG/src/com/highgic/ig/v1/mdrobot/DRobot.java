//package com.highgic.ig.v1.mdrobot;
//
//import java.io.File;
//import java.io.RandomAccessFile;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.os.Handler;
//import android.util.Log;
//
///**
// * 下载工具类
// */
//
//public class DRobot {
//
//    private final static int TASK_COUNT = 3;
//    private DFileInfo mDFileInfo;
//    private Handler mHandler;
//    private List<DTaskInfo> mTaskInfoList = new ArrayList<DTaskInfo>();
//    private List<DTaskThread> mDTaskThreadList = new ArrayList<DTaskThread>();
//    private Dao mDao;
//    private Context mContext;
//
//    public DRobot(Context context, DFileInfo dFileInfo, Handler handler) {
//        this.mHandler = handler;
//        this.mDFileInfo = dFileInfo;
//        this.mContext = context;
//        this.mDao = new Dao(context);
//
//        initDRobot();
//    }
//
//
//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//        mDao.closedb();
//    }
//
//    public DRobotDialog getDialog() {
//        return new DRobotDialog(mContext, null, null);
//    }
//
//    /**
//     * 启动下载
//     */
//    public void startDownload(int fid) {
//
//        for (DTaskInfo info : mTaskInfoList) {
//            DTaskThread task = new DTaskThread(mDao, info);
//            mDTaskThreadList.add(task);
//            task.start();
//        }
//        
//        DLog.d("DownloadRobot." + this.getClass().getSimpleName() + " startDownload... " + mTaskInfoList.toString());
//
//    }
//
//
//    /**
//     * 停止下载（未完成）
//     */
//    public void stopDownload(int fid) {
//
//        //TODO  停止下载
//
//    }
//
//
//    /**
//     * 清除下载任务信息
//     */
//    public void clearTaskInfo() {
//        mDao.clearTaskInfo();
//    }
//
//
//    private void initDRobot() {
//        if (mDao.isFirstTaskInfo()) {
//            try {
//                URL u = new URL(mDFileInfo.url);
//                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
//                conn.setConnectTimeout(5000);
//                mDFileInfo.size = conn.getContentLength();
//                File f = new File(mDFileInfo.localPath + "/" + mDFileInfo.fileName);
//                RandomAccessFile raf = new RandomAccessFile(f, "rwd");
//                raf.setLength(mDFileInfo.size);
//            } catch (Exception e) {
//                Log.w("DRobot." + this.getClass().getSimpleName(), e.getMessage());
//            }
//            getTaskInfos();
//            mDao.saveTaskInfos(mTaskInfoList);
//        } else {
//            this.mTaskInfoList = mDao.getTaskInfos();
//        }
//    }
//
//    private void getTaskInfos() {
//        DTaskInfo info = null;
//        int block = mDFileInfo.size / TASK_COUNT;
//
//        int start;
//        int end;
//
//        for (int i = 0; i < TASK_COUNT; i++) {
//            start = i * block;
//            if (i != (TASK_COUNT - 1)) {
//                end = (i + 1) * block - 1;
//            } else {
//                end = mDFileInfo.size - 1;
//            }
//
//            info = new DTaskInfo();
//            info.start = start;
//            info.end = end;
//            info.tid = i;
//            info.url = mDFileInfo.url;
//            info.localPathName = mDFileInfo.localPath + "/" + mDFileInfo.fileName;
//
//            mTaskInfoList.add(info);
//        }
//    }
//
//
//
//}
