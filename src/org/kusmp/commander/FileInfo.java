package org.kusmp.commander;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.swing.*;
import javax.swing.text.html.ImageView;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileInfo {

    private String name;
    private long date;
    private long size;
    private String displayedSize;
    private double computedSize;
    private String df;
    private javafx.scene.image.ImageView image;

    public FileInfo(String name, String date, long size, String displayedSize, javafx.scene.image.ImageView image) {
        this.name = name;
        this.df = date;
        this.size = size;
        this.image = image;
        if(displayedSize.equals("<DIR>")){
            this.displayedSize = displayedSize;
        }
        else if(size<1024)
        {
            computedSize = Math.round((double)size*100)/100;
            this.displayedSize = String.valueOf(computedSize) + "B";
        }
        else if (size<1024*1024){
            computedSize = Math.round((double)size*100)/(1024*100);
            this.displayedSize = String.valueOf(computedSize)+ "kB";
        }
        else {
            computedSize = Math.round((double)size*100)/(100*1024*1024);
            this.displayedSize = String.valueOf(computedSize) + "MB";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDisplayedSize() {
        return displayedSize;
    }

    public void setDisplayedSize(String displayedSize) {
        this.displayedSize = displayedSize;
    }

    public double getComputedSize() {
        return computedSize;
    }

    public void setComputedSize(double computedSize) {
        this.computedSize = computedSize;
    }

    public String getDf() {
        return df;
    }

    public void setDf(String df) {
        this.df = df;
    }

    public javafx.scene.image.ImageView getImage() {
        return image;
    }

    public void setImage(javafx.scene.image.ImageView image) {
        this.image = image;
    }
}
