package com.highgic.ig.v1.mdrobot;

/**
 * 下载文件信息类。
 *
 * <br>==========================
 * <br> 公司：优视科技-游戏中心
 * <br> 开发：zhouxy3@ucweb.com
 * <br> 创建时间：2013-1-4上午10:33:35
 * <br>==========================
 */
public class DFileInfo {
    /**
     * 文件编号。
     */
    public int fid;
    
    /**
     * 文件大小。
     */
    public int size;
    
    /**
     * 文件当前已下载大小。
     */
    public int current;
    
    /**
     * 文件下载地址。
     */
    public String url;
    
    /**
     * 文件本地存储目录。
     */
    public String localPath;
    
    /**
     * 文件本地存储名。
     */
    public String fileName;
}
