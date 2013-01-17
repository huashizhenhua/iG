//package com.highgic.ig.v1.mdrobot;
//
//import java.util.List;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.highgic.ig.R;
//
///**
// * 用途。
// *
// * <br>==========================
// * <br> 公司：优视科技-游戏中心
// * <br> 开发：zhouxy3@ucweb.com
// * <br> 创建时间：2013-1-4上午10:22:53
// * <br>==========================
// */
//public class DRobotDialog extends Dialog {
//
//    private Button okButton;
//    private Button cancelButton;
//
//    private String url;
//    private int count = 2;
//    private String localFilePath;
//
//    private DRobot dRobot;
//    private LinearLayout drobotProgressbarLayout;
//    private ProgressBar[] bars;
//    private int completeCount = 0;
//
//    private Context context;
//
//
//
//    public DRobotDialog(Context context, String url, String localFilePath) {
//        super(context);
//        this.context = context;
//        this.url = url;
//        this.localFilePath = localFilePath;
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //            RelativeLayout rootRelativeLayout = new RelativeLayout(context);
//        //            rootRelativeLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 200));
//        //            rootRelativeLayout.setGravity(Gravity.CENTER);
//        //
//        //            barsLayout = new LinearLayout(context);
//        //            barsLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//        //            barsLayout.setOrientation(LinearLayout.VERTICAL);
//        //
//        //
//        //            RelativeLayout buttonsRelativeLayout = new RelativeLayout(context);
//        //            RelativeLayout.LayoutParams buttonsLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        //            buttonsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        //            buttonsLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//        //            buttonsRelativeLayout.setLayoutParams(buttonsLayoutParams);
//        //
//        //
//        //            okButton = new Button(context);
//        //            okButton.setId(21);
//        //            RelativeLayout. LayoutParams okButtonLayoutParams = new RelativeLayout.LayoutParams(100, 60);
//        //            okButton.setLayoutParams(okButtonLayoutParams);
//        // 
//        //            okButton.setText("下载");
//        //            
//        //            
//        //            cancelButton = new Button(context);
//        //            cancelButton.setId(22);
//        //            cancelButton.setText("取消");
//        //            RelativeLayout. LayoutParams cancelButtonLayoutParams = new RelativeLayout.LayoutParams(100, 60);
//        //            cancelButtonLayoutParams.addRule(RelativeLayout.RIGHT_OF, 21);
//        //            cancelButtonLayoutParams.setMargins(60, 0, 0, 0);
//        //            cancelButton.setLayoutParams(cancelButtonLayoutParams);
//        //            
//        //            buttonsRelativeLayout.addView(okButton);
//        //            buttonsRelativeLayout.addView(cancelButton);
//        //
//        //            rootRelativeLayout.addView(barsLayout);
//        //            rootRelativeLayout.addView(buttonsRelativeLayout);
//
//        setContentView(R.layout.drobot_dialog);
//        onCreateFindViews(savedInstanceState);
//        onCreateSetListeners(savedInstanceState);
//    }
//
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        onCreateInitProgressbars(null);
//
//    }
//
//
//    protected void onCreateFindViews(Bundle savedInstanceState) {
//        okButton = (Button) findViewById(R.id.drobot_ok_button);
//        cancelButton = (Button) findViewById(R.id.drobot_cancel_button);
//
//        drobotProgressbarLayout = (LinearLayout) findViewById(R.id.drobot_progressbar_layout);
//    }
//
//    protected void onCreateSetListeners(Bundle savedInstanceState) {
//        okButton.setOnClickListener(listener);
//        cancelButton.setOnClickListener(listener);
//    }
//
//    protected OnRobotClickListener listener = new OnRobotClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//            case R.id.drobot_ok_button:
//               
//                    dRobot.startDownload();
//               
//                break;
//
//            case R.id.drobot_cancel_button:
//                
//                
//                dismiss();
//                break;
//            }
//        }
//    };
//
//
//    private class OnRobotClickListener implements android.view.View.OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//
//        }
//    }
//
//    protected void onCreateInitProgressbars(Bundle savedInstanceState) {
//
//        dRobot = new DRobot(url, count, localFilePath, context);
//        if (dRobot.taskInfos != null || !dRobot.taskInfos.isEmpty()) {
//            addProgressBars(dRobot.taskInfos);
//        }
//        
//    }
//
//
//    private void addProgressBars(List<DFileInfo> fileList) {
//        bars = new ProgressBar[dRobot.taskCount];
//        ProgressBar bar = null;
//
//        for (DFileInfo fileinfo : fileList) {
//            bar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 10);
//            params.topMargin = 10;
//            bar.setLayoutParams(params);
//            bar.setMax(info.end - info.start);
//            bar.setProgress(info.current);
//            bars[info.tid] = bar;
//            drobotProgressbarLayout.addView(bar);
//        }
//    }
//
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
//                    Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
//                    dRobot.clearTaskInfo();
//                    
//            }
//        }
//    };
//
//    private void processFinish() {
//        if (bars != null) {
//            for (ProgressBar bar : bars) {
//                drobotProgressbarLayout.removeView(bar);
//            }
//        }
//    }
//}