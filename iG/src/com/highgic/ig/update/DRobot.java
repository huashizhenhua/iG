package com.highgic.ig.update;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 下载工具类
 */

public class DRobot {
    private String downloadUrl;
    public int taskCount;
    public String localFileNamePath;
    public int fileLength;
    public Handler dHandler;
    public List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
    public List<DTask> dTasks = new ArrayList<DTask>();
    private Dao dao;

    public DRobot(String url, int count, String localFilePath, Handler h, Context ctx) {
        this.downloadUrl = url;
        this.taskCount = count;
        this.localFileNamePath = localFilePath;
        this.dHandler = h;
        this.dao = new Dao(ctx);
        initDRobot();
    }

    /**
     * 启动下载
     */
    public void startDownload() {
        for (TaskInfo info : taskInfos) {
            DTask task = new DTask(info.tid, info.start, info.end, info.current);
            dTasks.add(task);
            task.start();
        }
        DLog.d("DownloadRobot." + this.getClass().getSimpleName() + " startDownload... " + taskInfos.toString());
    }


    /**
     * 清除下载任务信息
     */
    public void clearTaskInfo() {
        dao.clearTaskInfo();
    }


    private void initDRobot() {
        if (dao.isFirst()) {
            try {
                URL u = new URL(downloadUrl);
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setConnectTimeout(5000);
                fileLength = conn.getContentLength();
                File f = new File(localFileNamePath);
                RandomAccessFile raf = new RandomAccessFile(f, "rwd");
                raf.setLength(fileLength);
            } catch (Exception e) {
                Log.w("DownloadRobot." + this.getClass().getSimpleName(), e.getMessage());
            }
            getTaskInfos();
            dao.saveTaskInfos(taskInfos);
        } else {
            this.taskInfos = dao.getTaskInfos();
        }
    }

    private void getTaskInfos() {
        TaskInfo info = null;
        int block = fileLength / taskCount;

        int start;
        int end;
        for (int i = 0; i < taskCount; i++) {
            start = i * block;
            if (i != (taskCount - 1)) {
                end = (i + 1) * block - 1;
            } else {
                end = fileLength - 1;
            }
            info = new TaskInfo();
            info.start = start;
            info.end = end;
            info.tid = i;
            taskInfos.add(info);
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
    public class DTask extends Thread {
        public int start;
        public int end;
        public int tid;
        public int current;


        public DTask(int tid, int startPos, int endPos, int amount) {
            this.tid = tid;
            this.start = startPos;
            this.end = endPos;
            this.current = amount;
        }


        public void run() {

            try {
                URL u = new URL(downloadUrl);
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setConnectTimeout(5000);

                conn.setRequestProperty("Range", "bytes=" + (start + current) + "-" + end);
                RandomAccessFile raf = new RandomAccessFile(new File(localFileNamePath), "rwd");

                raf.seek(start + current);

                InputStream is = conn.getInputStream();

                byte[] buffer = new byte[10240];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    raf.write(buffer, 0, len);

                    
                    dao.updateTaskInfo(tid, len);

                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putInt("tid", tid);
                    b.putInt("size", len);
                    msg.setData(b);
                    dHandler.sendMessage(msg);
                }
                is.close();
            } catch (Exception e) {
               
                Log.w("DownloadRobot." + this.getClass().getSimpleName(),"tid = "+tid + e.getMessage());
            }

        }
    }



    /**
     * 数据库操作DAO
     * 
     * @author zhouxy3
     * 
     */
    public class Dao {
        private DBHelper helper;
       

        public Dao(Context ctx) {
            helper = new DBHelper(ctx);
        }

        public boolean isFirst() {
            long t1 = System.currentTimeMillis();
            boolean value = false;
            String sql = "select count(*) from download_task_infos";
            SQLiteDatabase  db = helper.getReadableDatabase();
            Cursor cur = db.rawQuery(sql, null);

            if (cur.moveToFirst()) {
                int count = cur.getInt(0);
                value = (count == 0) ? true : false;
            }
            cur.close();
            db.close();
            long t2 = System.currentTimeMillis();

            DLog.d("DownloadRobot." + this.getClass().getSimpleName() + " isFirst : " + (t2 - t1) + "ms");

            return value;
        }


        public List<TaskInfo> getTaskInfos() {

            long t1 = System.currentTimeMillis();
            List<TaskInfo> list = new ArrayList<TaskInfo>();
            TaskInfo info = null;
            String sql = "select * from download_task_infos";
            SQLiteDatabase  db = helper.getReadableDatabase();
            Cursor cur = db.rawQuery(sql, null);

            while (cur.moveToNext()) {
                info = new TaskInfo();
                info.tid = cur.getInt(cur.getColumnIndex("_tid"));
                info.start = cur.getInt(cur.getColumnIndex("_start"));
                info.end = cur.getInt(cur.getColumnIndex("_end"));
                info.current = cur.getInt(cur.getColumnIndex("_current"));
                list.add(info);
            }
            cur.close();

            db.close();
            long t2 = System.currentTimeMillis();
            
            DLog.d("DownloadRobot." + this.getClass().getSimpleName() + " getTaskInfos : " + (t2 - t1) + "ms");

            return list;
        }


        public void saveTaskInfos(List<TaskInfo> taskInfos) {
            long t1 = System.currentTimeMillis();
            SQLiteDatabase  db = helper.getWritableDatabase();
            try {
                db.beginTransaction();
                String sql = "insert into download_task_infos(_tid,_start,_end,_current) values(?,?,?,?)";
                for (TaskInfo info : taskInfos) {
                    db.execSQL(sql, new String[] { info.tid + "", info.start + "", info.end + "", info.current + "" });
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            db.close();
            long t2 = System.currentTimeMillis();
           
            DLog.d("DownloadRobot." + this.getClass().getSimpleName() + " insert into download_task_infos : " + (t2 - t1) + "ms");

        }


        public void updateTaskInfo(int tid, int len) {
            
            long t1 = System.currentTimeMillis();
            String sql = "update download_task_infos set _current = _current + ? where _tid = ?";
            SQLiteDatabase  updatedb = helper.getReadableDatabase();
            updatedb.execSQL(sql, new String[] { len + "", tid + "" });
           
            long t2 = System.currentTimeMillis();
            DLog.d("DownloadRobot." + this.getClass().getSimpleName() + " update download_task_infos : " + (t2 - t1) + "ms");

        }


        public void clearTaskInfo() {
            long t1 = System.currentTimeMillis();
            String sql = "delete from download_task_infos";
            SQLiteDatabase  db = helper.getWritableDatabase();
            db.execSQL(sql);
            db.close();
            long t2 = System.currentTimeMillis();
            DLog.d("DownloadRobot." + this.getClass().getSimpleName() + " delete from download_task_infos : " + (t2 - t1) + "ms");

        }
        
        /**
         * 关闭数据库，只需一次调用即可
         */
        public void closedb(){
            
            SQLiteDatabase  db = helper.getWritableDatabase();
            db.close();
            
        }

    }



    /**
     * 数据库建库帮助类
     * 
     * @author zhouxy3@ucweb.com
     * 
     */
    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public DBHelper(Context context) {
            this(context, "download_robot.db", null, 1);
        }

        public void onCreate(SQLiteDatabase db) {

            long t1 = System.currentTimeMillis();
            String sql = "create table download_task_infos(_id integer primary key autoincrement," + "_tid int,_start int,_end int,_current int)";
            db.execSQL(sql);
            long t2 = System.currentTimeMillis();
            DLog.d("DownloadRobot." + this.getClass().getSimpleName() + " create table download_task_infos : " + (t2 - t1) + "ms");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


    public static class DLog {

        public static boolean isdebug = false;

        public static void i(String s) {
            if (isdebug) {
                Log.i("DLog", s);
            }
        }

        public static void w(String s) {
            if (isdebug) {
                Log.w("DLog", s);
            }
        }

        public static void e(String s) {
            if (isdebug) {
                Log.e("DLog", s);
            }
        }

        public static void d(String s) {
            if (isdebug) {
                Log.d("DLog", s);
            }
        }
    }


}
