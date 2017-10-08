package com.example.com.superiordownloader.Information;

/**
 * Created by 59771 on 2017/10/1.
 */

public class ThreadInfo{
    private int id;
    private String url;
    private int start;
    private int end;
   // private boolean isStop=false;//记录线程是否暂停

    private int finished;//线程当时下载位置
public ThreadInfo(){}
    public ThreadInfo(int id, String url, int start, int end, int finished) {
        super();
        this.id = id;
        this.url = url;
        this.start = start;
        this.end = end;
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }
   /* public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }*/
}
