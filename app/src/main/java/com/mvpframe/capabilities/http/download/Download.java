package com.mvpframe.capabilities.http.download;

import com.mvpframe.capabilities.http.api.Api;

import java.io.Serializable;

/**
 * 下载实体类
 * 备注:用户使用下载类需要继承此类
 * <p>
 * Data：2019/07/08
 *
 * @author yong
 */
@Table("download")
public class Download implements Serializable {

    //    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @PrimaryKey(" primary key autoincrement")
    @Column("_id")
    private Integer id;

    @NotNull
    @Column("localUrl")
    private String localUrl;//本地存储地址

    @NotNull
    @Column("serverUrl")
    private String serverUrl;//下载地址

    @NotNull
    @Column("totalSize")
    private long totalSize;//文件大小

    @Column("currentSize")
    private long currentSize;//当前大小

    @NotNull
    @Column("state")
    private State state = State.NONE;//下载状态

    @Ignore
    private Api api;//接口service

    @Ignore
    private DownloadCallback callback;//回调接口

    public Download() {
    }

    public Download(String url) {
        setServerUrl(url);
    }

    public Download(String url, DownloadCallback callback) {
        setServerUrl(url);
        setCallback(callback);
    }

    /**
     * 枚举下载状态
     */
    public enum State {
        NONE,           //无状态
        WAITING,        //等待
        LOADING,        //下载中
        PAUSE,          //暂停
        ERROR,          //错误
        FINISH,         //完成
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalUrl() {
        return localUrl == null ? "" : localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getServerUrl() {
        return serverUrl == null ? "" : serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public DownloadCallback getCallback() {
        return callback;
    }

    public void setCallback(DownloadCallback callback) {
        this.callback = callback;
    }
}
