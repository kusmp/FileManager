package org.kusmp.commander;

import com.sun.nio.file.ExtendedCopyOption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ListFilesAndDirectories {

    public static Map<String, List<FileInfo>> walk(String path, SimpleDateFormat format) {
        SimpleDateFormat df = format;
        File image;
        List<FileInfo> fileList = new ArrayList<>();
        List<FileInfo> directoriesList = new ArrayList<>();
        Map<String, List<FileInfo>> returnMap = new HashMap<>();
        File f = new File(path);
        File[] files = f.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                fileList.add(new FileInfo(file.getName(), df.format(file.lastModified()), file.length(), "FILE", displayIcon("src/org/kusmp/commander/icons/file(1).png")));
            } else if (file.isDirectory()) {
                directoriesList.add(new FileInfo(file.getName(), df.format(file.lastModified()), file.length(), "<DIR>", displayIcon("src/org/kusmp/commander/icons/folder(1).png")));
            }
        }
        returnMap.put("files", fileList);
        returnMap.put("directory", directoriesList);
        return returnMap;
    }

    public static ObservableList<File> listDrives() {
        File[] paths;
        paths = File.listRoots();
        List<File> drives = new ArrayList<>();
        for (File f : paths) {
            drives.add(f);
        }
        ObservableList<File> options = FXCollections.observableArrayList(drives);
        return options;
    }

    public static ObservableList<String> returnLanguages() {
        ObservableList<String> options = FXCollections.observableArrayList("PL", "EN");
        return options;
    }

    public static ImageView displayIcon(String path) {
        File image = new File(path);
        Image im = new Image(image.toURI().toString());
        ImageView imageView = new ImageView();
        imageView.setImage(im);
        return imageView;
    }

    public static void CopyFile(List<String> filePath, String destinationPath) throws InterruptedException {

        Path rootFile;
        File file;
        Path destFile = Paths.get(destinationPath);
        File dest = destFile.toFile();
        CopyOption[] et = new CopyOption[2];
        et[0] = ExtendedCopyOption.INTERRUPTIBLE;
        et[1] = StandardCopyOption.REPLACE_EXISTING;

        for (String var : filePath) {
            rootFile = Paths.get(var);
            file = rootFile.toFile();

            if (file.isFile()) {
                try {
                    Files.copy(rootFile, destFile.resolve(rootFile.getFileName()), et);
                } catch (IOException e) {
                    e.getStackTrace();
                }
                ;
            } else if (file.isDirectory()) {
                String path = destinationPath + "\\" + rootFile.getFileName();
                destFile = Paths.get(path);
                dest = destFile.toFile();
                try {
                    FileUtils.copyDirectory(file, dest);
                } catch (IOException e) {
                    e.getStackTrace();
                }

            }

        }

    }


    public static void MoveFile2(List<String> filesPath, String destinationPath) throws InterruptedException {

        File rootFile;
        Path rootPath;
        Path destPath = Paths.get(destinationPath);
        CopyOption[] et = new CopyOption[2];
        et[0] = ExtendedCopyOption.INTERRUPTIBLE;
        et[1] = StandardCopyOption.REPLACE_EXISTING;
        for (String var : filesPath) {
            rootFile = new File(var);
            rootPath = Paths.get(var);
            if (rootFile.isFile()) {
                try {
                    Files.copy(rootPath, destPath.resolve(rootPath.getFileName()), et);
                } catch (IOException e) {
                    e.getStackTrace();
                }
                //  rootFile.renameTo(new File(destinationPath+"\\"+rootFile.getName()));
                // rootFile.delete();
            } else if (rootFile.isDirectory()) {
                String path = destinationPath + "\\" + rootFile.getName();
                File destFile = new File(path);
                try {
                    FileUtils.moveDirectory(rootFile, destFile);
                } catch (IOException e) {
                    e.getStackTrace();
                }

            }
        }
    }

    public static void DeleteFile(String filePath) throws IOException {
        File file = new File(filePath);
        try {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void DeleteFile2(List<String> filePath) {

        for (String var : filePath) {
            File file = new File(var);
            try {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
