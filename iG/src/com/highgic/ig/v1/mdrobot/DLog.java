package com.highgic.ig.v1.mdrobot;

import android.util.Log;

/**
 * Drobot自定义Log输出工具。
 * 
 * <br>==========================
 * <br> 公司：优视科技-游戏中心
 * <br> 开发：zhouxy3@ucweb.com
 * <br> 创建时间：2013-1-4上午10:45:35
 * <br>==========================
 */

public class DLog {

    public static boolean isdebug = false;

    public static void i(String s) {
        if (isdebug) {
            Log.i("DLog", "DRobot V2 :" + s);
        }
    }

    public static void w(String s) {
        if (isdebug) {
            Log.w("DLog", "DRobot V2 :" + s);
        }
    }

    public static void e(String s) {
        if (isdebug) {
            Log.e("DLog", "DRobot V2 :" + s);
        }
    }

    public static void d(String s) {
        if (isdebug) {
            Log.d("DLog", "DRobot V2 :" + s);
        }
    }
}
