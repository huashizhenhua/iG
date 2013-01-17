package com.highgic.ig.v1.mdrobot;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库操作DAO。
 * 
 * <br>==========================
 * <br> 公司：优视科技-游戏中心
 * <br> 开发：zhouxy3@ucweb.com
 * <br> 创建时间：2013-1-4上午10:19:18
 * <br>==========================
 */
public class Dao {
    private DBHelper helper;


    public Dao(Context ctx) {
        helper = new DBHelper(ctx);
    }

    public boolean isFirstTaskInfo() {
        long t1 = System.currentTimeMillis();
        boolean value = false;
        String sql = "select count(*) from tb_dtaskinfos";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        if (cur.moveToFirst()) {
            int count = cur.getInt(0);
            value = (count == 0) ? true : false;
        }
        cur.close();
        long t2 = System.currentTimeMillis();

        DLog.d("DownloadRobot." + this.getClass().getSimpleName() + " isFirst : " + (t2 - t1) + "ms");

        return value;
    }


    public List<DTaskInfo> getTaskInfos() {

        long t1 = System.currentTimeMillis();
        List<DTaskInfo> list = new ArrayList<DTaskInfo>();
        DTaskInfo info = null;
        String sql = "select * from tb_dtaskinfos";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            info = new DTaskInfo();
            info.tid = cur.getInt(cur.getColumnIndex("_tid"));
            info.start = cur.getInt(cur.getColumnIndex("_start"));
            info.end = cur.getInt(cur.getColumnIndex("_end"));
            info.current = cur.getInt(cur.getColumnIndex("_current"));
            info.url = cur.getString(cur.getColumnIndex("_url"));
            info.localPathName = cur.getString(cur.getColumnIndex("_localPathName"));
            list.add(info);
        }
        cur.close();

        long t2 = System.currentTimeMillis();

        DLog.d("DownloadRobot." + this.getClass().getSimpleName() + " getTaskInfos : " + (t2 - t1) + "ms");

        return list;
    }


    public void saveTaskInfos(List<DTaskInfo> taskInfos) {
        long t1 = System.currentTimeMillis();
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.beginTransaction();
            String sql = "insert into tb_dtaskinfos(_tid,_start,_end,_current) values(?,?,?,?)";
            for (DTaskInfo info : taskInfos) {
                db.execSQL(sql, new String[] { info.tid + "", info.start + "", info.end + "", info.current + "" });
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        long t2 = System.currentTimeMillis();

        DLog.d("DownloadRobot." + this.getClass().getSimpleName() + " insert into tb_dtaskinfos : " + (t2 - t1) + "ms");

    }


    public void updateTaskInfo(int tid, int len) {

        long t1 = System.currentTimeMillis();
        String sql = "update tb_dtaskinfos set _current = _current + ? where _tid = ?";
        SQLiteDatabase updatedb = helper.getReadableDatabase();
        updatedb.execSQL(sql, new String[] { len + "", tid + "" });

        long t2 = System.currentTimeMillis();
        DLog.d("DownloadRobot." + this.getClass().getSimpleName() + " update tb_dtaskinfos : " + (t2 - t1) + "ms");

    }


    public void clearTaskInfo() {
        long t1 = System.currentTimeMillis();
        String sql = "delete from tb_dtaskinfos";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(sql);
        long t2 = System.currentTimeMillis();
        DLog.d("DownloadRobot." + this.getClass().getSimpleName() + " delete from tb_dtaskinfos : " + (t2 - t1) + "ms");

    }
    
    
    
    
    
    
    

    /**
     * 关闭数据库，只需一次调用即可
     */
    public void closedb() {

        helper.getWritableDatabase().close();
        helper.getReadableDatabase().close();

    }

}
