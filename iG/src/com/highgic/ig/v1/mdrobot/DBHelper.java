package com.highgic.ig.v1.mdrobot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库建库帮助类。
 * 
 * <br>==========================
 * <br> 公司：优视科技-游戏中心
 * <br> 开发：zhouxy3@ucweb.com
 * <br> 创建时间：2013-1-4上午10:21:09
 * <br>==========================
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context) {
        this(context, "drobot.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {

        long t1 = System.currentTimeMillis();
        
        String sql = "create table tb_DTaskInfos(_id integer primary key autoincrement," + "_tid int,_start int,_end int,_current int,_url string,_localPathName string)";
        db.execSQL(sql);
        
        
        String sql2 = "create table tb_DFileInfos(_id integer primary key autoincrement," + "_fid int,_size int,_current int,_url string,_localPath string,_fileName string)";
        db.execSQL(sql2);
        
        
        long t2 = System.currentTimeMillis();
        DLog.d("DRobot." + this.getClass().getSimpleName() + " create table tb_DTaskInfos,tb_DFileInfos : " + (t2 - t1) + "ms");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
