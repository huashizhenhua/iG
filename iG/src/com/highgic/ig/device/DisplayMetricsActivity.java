package com.highgic.ig.device;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.highgic.ig.R;



public class DisplayMetricsActivity extends Activity {

    private static String TAG = "DisplayMetricsActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_metrics);
        
        setTitle("iG: Display Metrics");

        
        StringBuilder sb = new StringBuilder();

        // 获取屏幕密度（方法2）  
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();

        float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）  
        int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）  
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;

        Log.e(TAG + "  DisplayMetrics", "xdpi=" + xdpi + "; ydpi=" + ydpi);
        Log.e(TAG + "  DisplayMetrics", "density=" + density + "; densityDPI=" + densityDPI);
        
        sb.append("xdpi=" + xdpi + "; ydpi=" + ydpi+"\n");
        sb.append("density=" + density + "; densityDPI=" + densityDPI+"\n");
        
        
        int screenWidthPixels = dm.widthPixels; // 屏幕宽（像素，如：480px）  
        int screenHeightPixels = dm.heightPixels; // 屏幕高（像素，如：800px）  

        Log.e(TAG + "  DisplayMetrics(111)", "screenWidthPixels=" + screenWidthPixels + "; screenHeightPixels=" + screenHeightPixels);
        sb.append("screenWidthPixels=" + screenWidthPixels + "; screenHeightPixels=" + screenHeightPixels+"\n");

        int screenDensityWidth = (int) (dm.widthPixels / density + 0.5f); // 屏幕宽（dip，如：533dip）  
        int screenDensityHeight = (int) (dm.heightPixels / density + 0.5f); // 屏幕高（dip，如：800dip）  

        Log.e(TAG + "  DisplayMetrics(222)", "screenDensityWidth=" + screenDensityWidth + "; screenDensityHeight=" + screenDensityHeight);
        sb.append("screenDensityWidth=" + screenDensityWidth + "; screenDensityHeight=" + screenDensityHeight+"\n");

        
        TextView displayMetricsTextView = (TextView) findViewById(R.id.display_metrics_text);
        
        displayMetricsTextView.setText(sb.toString());

    }

}
