package org.kusmp.commander;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {

    @FXML ComboBox cbPartition1;
    @FXML ComboBox cbPartition2;
    @FXML ComboBox cbChangeLang;
    @FXML javafx.scene.control.TableView leftTable;
    @FXML javafx.scene.control.TableView rightTable;
    @FXML TableColumn<FileInfo, ImageView> leftIcon;
    @FXML TableColumn leftName;
    @FXML TableColumn leftSize;
    @FXML TableColumn leftModified;
    @FXML TableColumn<FileInfo, ImageView> rightIcon;
    @FXML TableColumn rightName;
    @FXML TableColumn rightSize;
    @FXML TableColumn rightModified;
    @FXML Label leftLabel;
    @FXML Label rightLabel;
    @FXML Button btnSelect1;
    @FXML Button btnSelect2;

    @FXML
    public void initialize(){
        ObservableList<File> cbPartition1List = ListFilesAndDirectories.listDrives();
        cbPartition1.getItems().addAll(cbPartition1List);
        cbPartition1.getSelectionModel().selectFirst();
        cbPartition2.getItems().addAll(cbPartition1List);
        cbPartition2.getSelectionModel().select(1);
        cbChangeLang.getItems().addAll(ListFilesAndDirectories.returnLanguages());
        cbChangeLang.getSelectionModel().selectFirst();
        leftIcon.setCellValueFactory(new PropertyValueFactory<FileInfo,ImageView>("image"));
        leftName.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("name"));
        leftSize.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("displayedSize"));
        leftModified.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("df"));
        rightIcon.setCellValueFactory(new PropertyValueFactory<FileInfo,ImageView>("image"));
        rightName.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("name"));
        rightSize.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("displayedSize"));
        rightModified.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("df"));
        ObservableList<FileInfo> list = fillTable("C:\\");
        leftTable.setItems(list);
        list = fillTable("D:\\");
        rightTable.setItems(list);
        leftLabel.setText(cbPartition1.getSelectionModel().getSelectedItem().toString());
        rightLabel.setText(cbPartition2.getSelectionModel().getSelectedItem().toString());
        btnSelect1.setDisable(true);
        btnSelect2.setDisable(true);

    }



    public ObservableList<FileInfo> fillTable(String path){
        Map<String, List<FileInfo>> obj = ListFilesAndDirectories.walk(path);
        List<FileInfo> files = new ArrayList<>();
        List<FileInfo> directories = new ArrayList<>();
        for(Map.Entry<String, List<FileInfo>> entry : obj.entrySet()){
            if(entry.getKey().equals("files")){
                files = entry.getValue();
            }
            else if(entry.getKey().equals("directory")){
                directories = entry.getValue();
            }
        }
        List<FileInfo> merged = new ArrayList<>(files);
        merged.addAll(directories);
        ObservableList<FileInfo> column = FXCollections.observableArrayList(merged);
        return column;
    }


    public void cb1Changed(javafx.event.ActionEvent event) {
       String path =  cbPartition1.getSelectionModel().getSelectedItem().toString();
       leftLabel.setText(path);
      leftTable.setItems(fillTable(path));
      btnSelect1.setDisable(true);
    }

    public void cb2Changed(javafx.event.ActionEvent event) {
        String path =  cbPartition2.getSelectionModel().getSelectedItem().toString();
        rightLabel.setText(path);
        rightTable.setItems(fillTable(path));
    }


    public void clickLeftRow(javafx.scene.input.MouseEvent mouseEvent) {
        String path = cbPartition1.getSelectionModel().getSelectedItem().toString();
        if(mouseEvent.getClickCount() == 2){
            btnSelect1.setDisable(false);
            path = leftLabel.getText();
            if(leftTable.getSelectionModel().getSelectedItem() != null) {
                FileInfo selectedDirectory = (FileInfo)leftTable.getSelectionModel().getSelectedItem();
                path = path + "\\" + selectedDirectory.getName();
                leftLabel.setText(path);
                leftTable.setItems(fillTable(path));
            }
        }
    }


    public void clickRightRow(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        String path = cbPartition2.getSelectionModel().getSelectedItem().toString();
        if(mouseEvent.getClickCount() == 2){
            btnSelect2.setDisable(false);
            path = rightLabel.getText();
            FileInfo obj = (FileInfo)rightTable.getSelectionModel().getSelectedItem();
            if(obj.getDisplayedSize().equals("<DIR>")) {
                FileInfo selectedDirectory = (FileInfo)rightTable.getSelectionModel().getSelectedItem();
                path = path + "\\" + selectedDirectory.getName();
                rightLabel.setText(path);
                rightTable.setItems(fillTable(path));
            }
            else{
                FileInfo selectedDirectory = (FileInfo)rightTable.getSelectionModel().getSelectedItem();
                path = path + "\\" + selectedDirectory.getName();
                File f = new File(path);
                Desktop.getDesktop().open(f);
            }
        }
    }


    public void btnUp1Clicked(ActionEvent event) {
        String path = leftLabel.getText();
        if(!path.equals(cbPartition1.getSelectionModel().getSelectedItem().toString())) {
        int index = path.lastIndexOf("\\");
        if(index > 0) {
            path = path.substring(0, index);
            leftLabel.setText(path);
            leftTable.setItems(fillTable(path));
        }
        }
    }

    public void btnUp2Clicked(javafx.event.ActionEvent event) {
        String path = rightLabel.getText();
        if(!path.equals(cbPartition2.getSelectionModel().getSelectedItem())) {
            int index = path.lastIndexOf("\\");
            if (index > 0) {
                path = path.substring(0, index);
                rightLabel.setText(path);
                rightTable.setItems(fillTable(path));
            }
        }
    }

}
