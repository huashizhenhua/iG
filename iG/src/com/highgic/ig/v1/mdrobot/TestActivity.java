//package com.highgic.ig.v1.mdrobot;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.Toast;
//
//
///**
// * MainActivity
// */
//public class TestActivity extends Activity implements OnClickListener {
//
//    private static String url = "http://wap3.ucweb.com/files/UCBrowser/zh-cn/999/UCBrowser_V8.7.4.225_Android_pf145_(Build12121509).apk";
//    private static String localFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "UCBrowser_V8.7.4.225_Android_pf145_(Build12121509).apk";
//
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//
//    }
//
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == 1) {
//
//            new DRobotDialog(this, url, localFilePath).show();
//        }
//    }
//
//    private Handler h = new Handler() {
//
//        public void handleMessage(Message msg) {
//            Bundle b = msg.getData();
//            int tid = b.getInt("tid");
//            int fid = b.getInt("fid");
//            int size = b.getInt("size");
//
//            bars[fid].setProgress(bars[fid].getProgress() + size);
//
//            if (bars[fid].getProgress() == bars[fid].getMax()) {
//
//                Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
//                dRobot.clearTaskInfo();
//
//            }
//        }
//    };
//
//}