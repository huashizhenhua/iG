package com.highgic.ig.v1.mdrobot;

/**
 * 下载任务信息实体类。
 * 
 * <br>==========================
 * <br> 公司：优视科技-游戏中心
 * <br> 开发：zhouxy3@ucweb.com
 * <br> 创建时间：2013-1-4上午10:16:30
 * <br>==========================
 */

public class DTaskInfo {
    
    /**
     * 任务编号。
     */
    public int tid;
    
    
    /**
     * 文件编号。
     */
    public int fid;
    
    /**
     * 任务开始于start字节。
     */
    public int start;
    
    /**
     * 任务结束于end字节。
     */
    public int end;
    
    /**
     * 当前下载完成的字节数。
     */
    public int current;
    
    /**
     * 下载地址。
     */
    public String url;
    
    /**
     * 本地存储路径，包括文件名。
     */
    public String localPathName; 
}
