package org.kusmp.commander;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListFilesAndDirectories {

    public static Map<String, List<FileInfo>> walk(String path){
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        File image;
        List<FileInfo> fileList = new ArrayList<>();
        List<FileInfo> directoriesList = new ArrayList<>();
        Map<String, List<FileInfo>> returnMap = new HashMap<>();
        File f = new File(path);
        File[] files = f.listFiles();
        for (File file : files){
            if(file.isFile()){
            fileList.add(new FileInfo(file.getName(),  df.format(file.lastModified()), file.length(),"FILE", displayIcon("src/org/kusmp/commander/icons/file.png")));
            }
            else if(file.isDirectory()){
                directoriesList.add(new FileInfo(file.getName(), df.format(file.lastModified()), file.length(), "<DIR>", displayIcon("src/org/kusmp/commander/icons/folder.png")));
            }
        }
        returnMap.put("files", fileList);
        returnMap.put("directory", directoriesList);
        return returnMap;
    }

    public static ObservableList<File> listDrives(){
        File[] paths;
        paths = File.listRoots();
        List<File> drives = new ArrayList<>();
        for(File f : paths){
            drives.add(f);
        }
        ObservableList<File> options = FXCollections.observableArrayList(drives);
        return options;
    }

    public static ObservableList<String> returnLanguages(){
        ObservableList<String> options = FXCollections.observableArrayList("PL", "EN");
        return options;
    }

    public static ImageView displayIcon(String path){
        File image = new File(path);
        Image im = new Image(image.toURI().toString());
        ImageView imageView = new ImageView();
        imageView.setImage(im);
        return imageView;
    }


}
