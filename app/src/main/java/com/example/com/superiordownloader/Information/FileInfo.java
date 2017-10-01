package com.example.com.superiordownloader.Information;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by 59771 on 2017/9/30.
 */

public class FileInfo extends DataSupport implements Serializable {


    private int id;
    private String url;
    private String fileName;
    private int length;
    private int finished;//文件完成度

    private double speed=0.0;//下载速度

    public FileInfo() {
        super();
    }

    public FileInfo(int id, String url, String fileName, int length, int finished) {
        super();
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.length = length;
        this.finished = finished;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }


    @Override
    public String toString() {
        return "FileInfo [id=" + id + ", url=" + url + ", fileName=" + fileName + ", length=" + length + ", finished="
                + finished + "]";
    }
}
