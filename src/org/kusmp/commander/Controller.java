package org.kusmp.commander;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements Initializable {

    @FXML
    ComboBox cbPartition1;
    @FXML
    ComboBox cbPartition2;
    @FXML
    ComboBox cbChangeLang;
    @FXML
    javafx.scene.control.TableView leftTable;
    @FXML
    javafx.scene.control.TableView rightTable;
    @FXML
    TableColumn<FileInfo, ImageView> leftIcon;
    @FXML
    TableColumn leftName;
    @FXML
    TableColumn leftSize;
    @FXML
    TableColumn leftModified;
    @FXML
    TableColumn<FileInfo, ImageView> rightIcon;
    @FXML
    TableColumn rightName;
    @FXML
    TableColumn rightSize;
    @FXML
    TableColumn rightModified;
    @FXML
    Label leftLabel;
    @FXML
    Label rightLabel;
    @FXML
    Button btnSelect1;
    @FXML
    Button btnSelect2;
    @FXML
    Button btnCopy;
    @FXML
    Button btnMove;
    @FXML
    Button btnDelete;
    @FXML
    ProgressBar pb;
    @FXML
    ProgressIndicator pi;
    @FXML
    Button btnCancel;



    ButtonType button_tak = new ButtonType("Tak", ButtonBar.ButtonData.OK_DONE);
    ButtonType button_nie = new ButtonType("Nie", ButtonBar.ButtonData.CANCEL_CLOSE);

    private ResourceBundle bundle;
    private Locale locale;
    SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private String localeAlertMessage;
    private String headerTitle="Potwierdzenie";
    private String moveAlertMessage;
    private String btnCancelClose;
    private String btnCancelText;
    private String piDoneText;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        leftSize.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                o1 = o1.replaceAll(",", ".");
                o2 = o2.replaceAll(",", ".");
                Double doubleSize1 = new Double(0);

                if (o1.contains("kB")) {
                    o1 = o1.replaceAll("kB", "");
                    doubleSize1 = Double.parseDouble(o1) * 1024f;
                } else if (o1.contains("MB")) {
                    o1 = o1.replaceAll("MB", "");
                    doubleSize1 = Double.parseDouble(o1) * 1024f * 1024f;
                } else if (o1.contains("B")) {
                    o1 = o1.replaceAll("B", "");
                    doubleSize1 = Double.parseDouble(o1);
                }

                Double doubleSize2 = new Double(0);
                if (o2.contains("kB")) {
                    o2 = o2.replaceAll("kB", "");
                    doubleSize2 = Double.parseDouble(o2) * 1024f;
                } else if (o2.contains("MB")) {
                    o2 = o2.replaceAll("MB", "");
                    doubleSize2 = Double.parseDouble(o2) * 1024f * 1024f;
                } else if (o2.contains("B")) {
                    o2 = o2.replaceAll("B", "");
                    doubleSize2 = Double.parseDouble(o2);
                }

                return doubleSize1.compareTo(doubleSize2);
            }
        });

        rightSize.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                o1 = o1.replaceAll(",", ".");
                o2 = o2.replaceAll(",", ".");
                Double doubleSize1 = new Double(0);

                if (o1.contains("kB")) {
                    o1 = o1.replaceAll("kB", "");
                    doubleSize1 = Double.parseDouble(o1) * 1024f;
                } else if (o1.contains("MB")) {
                    o1 = o1.replaceAll("MB", "");
                    doubleSize1 = Double.parseDouble(o1) * 1024f * 1024f;
                } else if (o1.contains("B")) {
                    o1 = o1.replaceAll("B", "");
                    doubleSize1 = Double.parseDouble(o1);
                }

                Double doubleSize2 = new Double(0);
                if (o2.contains("kB")) {
                    o2 = o2.replaceAll("kB", "");
                    doubleSize2 = Double.parseDouble(o2) * 1024f;
                } else if (o2.contains("MB")) {
                    o2 = o2.replaceAll("MB", "");
                    doubleSize2 = Double.parseDouble(o2) * 1024f * 1024f;
                } else if (o2.contains("B")) {
                    o2 = o2.replaceAll("B", "");
                    doubleSize2 = Double.parseDouble(o2);
                }

                return doubleSize1.compareTo(doubleSize2);
            }
        });

        ObservableList<File> cbPartition1List = ListFilesAndDirectories.listDrives();
        cbPartition1.getItems().addAll(cbPartition1List);
        cbPartition1.getSelectionModel().selectFirst();
        cbPartition2.getItems().addAll(cbPartition1List);
        cbPartition2.getSelectionModel().select(1);
        cbChangeLang.getItems().addAll(ListFilesAndDirectories.returnLanguages());
        cbChangeLang.getSelectionModel().selectFirst();
        leftTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        rightTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        leftIcon.setCellValueFactory(new PropertyValueFactory<FileInfo, ImageView>("image"));
        leftName.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("name"));
        leftSize.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("displayedSize"));
        leftModified.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("df"));
        rightIcon.setCellValueFactory(new PropertyValueFactory<FileInfo, ImageView>("image"));
        rightName.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("name"));
        rightSize.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("displayedSize"));
        rightModified.setCellValueFactory(new PropertyValueFactory<FileInfo, String>("df"));
        leftIcon.setStyle("-fx-alignment: CENTER;");
        rightIcon.setStyle("-fx-alignment: CENTER;");
        leftName.setStyle("-fx-alignment: CENTER-LEFT;");
        rightName.setStyle("-fx-alignment: CENTER-LEFT;");
        leftSize.setStyle( "-fx-alignment: CENTER;");
        leftModified.setStyle( "-fx-alignment: CENTER;");
        rightSize.setStyle( "-fx-alignment: CENTER;");
        rightModified.setStyle( "-fx-alignment: CENTER;");
        ObservableList<FileInfo> list = fillTable("C:\\");
        leftTable.setItems(list);
        list = fillTable("D:\\");
        rightTable.setItems(list);
        leftLabel.setText(cbPartition1.getSelectionModel().getSelectedItem().toString());
        rightLabel.setText(cbPartition2.getSelectionModel().getSelectedItem().toString());
        btnSelect1.setDisable(true);
        btnSelect2.setDisable(true);
        pb.setVisible(false);
        pi.setVisible(false);
        btnCancel.setVisible(false);
        localeAlertMessage = "Próbujesz usunąć plik. Jest to operacja nieodwracalna. Czy chcesz kontynuować?";
        leftTable.setPlaceholder(new Label("Brak zawartości do wyświetlenia"));
        rightTable.setPlaceholder(new Label("Brak zawartości do wyświetlenia"));
        moveAlertMessage = "Plik o takiej nazwie istnieje. Czy chcesz go nadpisać?";
        btnCancelText = "Anuluj";
        btnCancelClose = "Zamknij";
        piDoneText = "Zrobione!";
        leftTable.setFixedCellSize(35.0);
        rightTable.setFixedCellSize(35.0);
    }

    public void activateProgressBar(final Task<?> task) {
        pb.progressProperty().unbind();
        pi.progressProperty().unbind();
        pb.setVisible(true);
        pi.setVisible(true);
        btnCancel.setVisible(true);
        pb.setProgress(-1F);
        pi.setProgress(-1F);
        pb.progressProperty().bind(task.progressProperty());
        pi.progressProperty().bind(task.progressProperty());
        pi.progressProperty().addListener((ov, oldValue, newValue) -> {
            Text text = (Text) pi.lookup(".percentage");
            if(text!=null && text.getText().equals("Done")){
                text.setText(piDoneText);
                pi.setPrefWidth(text.getLayoutBounds().getWidth());
            }
        });
    }


    public ObservableList<FileInfo> fillTable(String path) {


        final CountDownLatch latch = new CountDownLatch(1);
        final ObservableList<FileInfo>[] value = new ObservableList[1];
        try {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, List<FileInfo>> obj = ListFilesAndDirectories.walk(path, df);
                    List<FileInfo> files = new ArrayList<>();
                    List<FileInfo> directories = new ArrayList<>();

                    for (Map.Entry<String, List<FileInfo>> entry : obj.entrySet()) {
                        if (entry.getKey().equals("files")) {
                            files = entry.getValue();
                        } else if (entry.getKey().equals("directory")) {
                            directories = entry.getValue();
                        }
                    }
                    List<FileInfo> merged = new ArrayList<>(files);
                    merged.addAll(directories);
                    ObservableList<FileInfo> column = FXCollections.observableArrayList(merged);
                    value[0] = column;
                    latch.countDown();
                }
            });
            t1.start();
            //  try {
            latch.await();
            // } catch (Exception e) {
            //    e.printStackTrace();
            //  }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Map<String, List<FileInfo>> obj = ListFilesAndDirectories.walk(path, df);
//        List<FileInfo> files = new ArrayList<>();
//        List<FileInfo> directories = new ArrayList<>();
//
//        for (Map.Entry<String, List<FileInfo>> entry : obj.entrySet()) {
//            if (entry.getKey().equals("files")) {
//                files = entry.getValue();
//            } else if (entry.getKey().equals("directory")) {
//                directories = entry.getValue();
//            }
//        }
//        List<FileInfo> merged = new ArrayList<>(files);
//        merged.addAll(directories);
//        ObservableList<FileInfo> column = FXCollections.observableArrayList(merged);
        return value[0];
    }


    public void cb1Changed(javafx.event.ActionEvent event) {
        String path = cbPartition1.getSelectionModel().getSelectedItem().toString();
        leftLabel.setText(path);
        leftTable.setItems(fillTable(path));
        btnSelect1.setDisable(true);
    }

    public void cb2Changed(javafx.event.ActionEvent event) {
        String path = cbPartition2.getSelectionModel().getSelectedItem().toString();
        rightLabel.setText(path);
        rightTable.setItems(fillTable(path));
        btnSelect2.setDisable(true);
    }

    public void cbChangeLangChanged(ActionEvent event) {
        if (cbChangeLang.getSelectionModel().getSelectedItem().toString().equals("EN")) {
            loadLang("en");
            leftTable.setPlaceholder(new Label("No content to display"));
            rightTable.setPlaceholder(new Label("No content to display"));
            df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
            String leftPath = leftLabel.getText();
            ObservableList<FileInfo> list = fillTable(leftPath);
            leftTable.setItems(list);
            String rightPath = rightLabel.getText();
            list = fillTable(rightPath);
            rightTable.setItems(list);
            button_tak = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            button_nie = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        } else if (cbChangeLang.getSelectionModel().getSelectedItem().toString().equals("PL")) {
            loadLang("pl");
            leftTable.setPlaceholder(new Label("Brak zawartości do wyświetlenia"));
            rightTable.setPlaceholder(new Label("Brak zawartości do wyświetlenia"));
            df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            String leftPath = leftLabel.getText();
            ObservableList<FileInfo> list = fillTable(leftPath);
            leftTable.setItems(list);
            String rightPath = rightLabel.getText();
            list = fillTable(rightPath);
            rightTable.setItems(list);
            button_tak = new ButtonType("Tak", ButtonBar.ButtonData.OK_DONE);
            button_nie = new ButtonType("Nie", ButtonBar.ButtonData.CANCEL_CLOSE);
        }
    }


    public void clickLeftRow(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        rightTable.getSelectionModel().clearSelection();
        String path = cbPartition1.getSelectionModel().getSelectedItem().toString();
        if (mouseEvent.getClickCount() == 2) {
            btnSelect1.setDisable(false);
            path = leftLabel.getText();
            if (leftTable.getSelectionModel().getSelectedItem() != null) {
                FileInfo obj = (FileInfo) leftTable.getSelectionModel().getSelectedItem();
                if (obj.getDisplayedSize().equals("<DIR>")) {
                    FileInfo selectedDirectory = (FileInfo) leftTable.getSelectionModel().getSelectedItem();
                    path = path + "\\" + selectedDirectory.getName();
                    leftLabel.setText(path);
                    leftTable.setItems(fillTable(path));
                } else {
                    FileInfo selectedDirectory = (FileInfo) leftTable.getSelectionModel().getSelectedItem();
                    path = path + "\\" + selectedDirectory.getName();
                    File f = new File(path);
                    Desktop.getDesktop().open(f);
                }
            }
        }
    }


    public void clickRightRow(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        leftTable.getSelectionModel().clearSelection();
        String path = cbPartition2.getSelectionModel().getSelectedItem().toString();
        if (mouseEvent.getClickCount() == 2) {
            btnSelect2.setDisable(false);
            path = rightLabel.getText();
            FileInfo obj = (FileInfo) rightTable.getSelectionModel().getSelectedItem();
            if (obj.getDisplayedSize().equals("<DIR>")) {
                FileInfo selectedDirectory = (FileInfo) rightTable.getSelectionModel().getSelectedItem();
                path = path + "\\" + selectedDirectory.getName();
                rightLabel.setText(path);
                rightTable.setItems(fillTable(path));
            } else {
                FileInfo selectedDirectory = (FileInfo) rightTable.getSelectionModel().getSelectedItem();
                path = path + "\\" + selectedDirectory.getName();
                File f = new File(path);
                Desktop.getDesktop().open(f);
            }
        }
    }


    public void btnUp1Clicked(ActionEvent event) {
        String path = leftLabel.getText();
        int index = path.lastIndexOf("\\");
        if (index > 0) {
            path = path.substring(0, index);
            leftLabel.setText(path);
            if (path.equals(cbPartition1.getSelectionModel().getSelectedItem().toString())) {
                btnSelect1.setDisable(true);
            }
            leftTable.setItems(fillTable(path));
        }

    }

    public void btnUp2Clicked(javafx.event.ActionEvent event) {
        String path = rightLabel.getText();
        int index = path.lastIndexOf("\\");
        if (index > 0) {
            path = path.substring(0, index);
            rightLabel.setText(path);
            if (path.equals(cbPartition2.getSelectionModel().getSelectedItem().toString())) {
                btnSelect2.setDisable(true);
            }
            rightTable.setItems(fillTable(path));
        }
    }


    public void copyTaskSettings(String destPath, List<String> deletePaths, Task<Void> task, Thread thread, boolean remove) {

        btnCopy.setDisable(true);
        btnMove.setDisable(true);
        btnDelete.setDisable(true);
        btnCancel.setDisable(false);
        activateProgressBar(task);
        task.setOnSucceeded(tEvent -> {
            System.out.println("Success");
            ObservableList<FileInfo> list = fillTable(rightLabel.getText());
            rightTable.setItems(list);
            btnCopy.setDisable(false);
            btnMove.setDisable(false);
            btnDelete.setDisable(false);
            // btnCancel.setDisable(true);
            btnCancel.setText(btnCancelClose);
            btnCancel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    pb.setVisible(false);
                    pi.setVisible(false);
                    btnCancel.setVisible(false);
                }
            });
        });
        task.setOnFailed(tEvent -> {
            System.out.println("Failed");
            ObservableList<FileInfo> list = fillTable(destPath);
            rightTable.setItems(list);
            // btnCancel.setDisable(true);
            btnCancel.setText(btnCancelClose);
            btnCancel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    pb.setVisible(false);
                    pi.setVisible(false);
                    btnCancel.setVisible(false);
                }
            });

        });

        task.setOnCancelled(tEvent -> {
            System.out.println("cancelled2");
            btnCopy.setDisable(false);
            btnMove.setDisable(false);
            btnDelete.setDisable(false);
            // btnCancel.setDisable(true);
            List<String> directoriesToDelete = new ArrayList<>();
            for (String direct : deletePaths) {
                File fi = new File(direct);
                if (fi.isDirectory()) {
                    directoriesToDelete.add(direct);
                }
            }
            if (!directoriesToDelete.isEmpty()) {
                ListFilesAndDirectories.DeleteFile2(directoriesToDelete);
            }
            ObservableList<FileInfo> list = fillTable(destPath);
            if (remove) {
                rightTable.setItems(list);
            }
            pb.progressProperty().unbind();
            pb.setProgress(0);
            pi.progressProperty().unbind();
            pi.setProgress(0);
            btnCancel.setText(btnCancelClose);
            btnCancel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    pb.setVisible(false);
                    pi.setVisible(false);
                    btnCancel.setVisible(false);
                }
            });

        });

        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Wszedlem");
                thread.interrupt();
            }
        });
    }


    public void btnCopyClicked(ActionEvent event) throws IOException {

        btnCancel.setDisable(false);
        btnCancel.setText(btnCancelText);
        boolean status = false;
        File f;
        String destPath = rightLabel.getText();
        List<String> paths = new ArrayList<>();
        List<String> deletePaths = new ArrayList<>();
        ObservableList<FileInfo> selectedItems = leftTable.getSelectionModel().getSelectedItems();
        for (FileInfo row : selectedItems) {
            paths.add(leftLabel.getText() + "\\" + row.getName());
            deletePaths.add(rightLabel.getText() + "\\" + row.getName());
            f = new File(rightLabel.getText() + "\\" + row.getName());
            if (f.exists()) {
                status = true;
            }
        }

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws IOException {
                try {
                    ListFilesAndDirectories.CopyFile(paths, destPath);
                    Thread.sleep(1);
                    updateProgress(10, 10);
                } catch (InterruptedException e) {
                    System.out.println("Złapałem");
                    this.cancel(true);
                }

                return null;
            }
        };

        if (!status) { //jeśli plik jest nowy i nie istnieje w folderze docelowym

            Thread thread = new Thread(task);
            copyTaskSettings(destPath, deletePaths, task, thread, true);
            thread.start();

        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, moveAlertMessage, button_tak, button_nie);
            alert.setHeaderText(null);
            alert.setTitle(headerTitle);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.showAndWait();
            if (alert.getResult() == button_tak) {

                Thread thread = new Thread(task);
                copyTaskSettings(destPath, deletePaths, task, thread, false);
                thread.start();

            }
        }
    }

    public void moveTaskSettings(List<String> rootPaths, List<String> paths, Thread thread, Task<Void> task, boolean remove) {
        btnCopy.setDisable(true);
        btnMove.setDisable(true);
        btnDelete.setDisable(true);
        btnCancel.setDisable(false);
        activateProgressBar(task);
        task.setOnSucceeded(tEvent -> {
            System.out.println("Success");
            ListFilesAndDirectories.DeleteFile2(rootPaths);
            ObservableList<FileInfo> list = fillTable(rightLabel.getText());
            rightTable.setItems(list);
            leftTable.setItems(fillTable(leftLabel.getText()));
            btnCopy.setDisable(false);
            btnMove.setDisable(false);
            btnDelete.setDisable(false);
            //btnCancel.setDisable(true);
            btnCancel.setText(btnCancelClose);
            btnCancel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    pb.setVisible(false);
                    pi.setVisible(false);
                    btnCancel.setVisible(false);
                }
            });

        });

        task.setOnFailed(tEvent -> {
            System.out.println("Failed");
            ObservableList<FileInfo> list = fillTable(rightLabel.getText());
            rightTable.setItems(list);
            leftTable.setItems(fillTable(leftLabel.getText()));
            btnCopy.setDisable(false);
            btnMove.setDisable(false);
            btnDelete.setDisable(false);
            //btnCancel.setDisable(true);
            btnCancel.setText(btnCancelClose);
            btnCancel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    pb.setVisible(false);
                    pi.setVisible(false);
                    btnCancel.setVisible(false);
                }
            });

        });

        task.setOnCancelled(tEvent -> {
            System.out.println("cancelled2");
            List<String> directoriesToDelete = new ArrayList<>();
            for (String direct : paths) {
                File f = new File(direct);
                if (f.isDirectory()) {
                    directoriesToDelete.add(direct);
                }
            }
            if (!directoriesToDelete.isEmpty()) {
                ListFilesAndDirectories.DeleteFile2(directoriesToDelete);
            }
            ObservableList<FileInfo> list = fillTable(rightLabel.getText());
            rightTable.setItems(list);
            if (remove) {
                leftTable.setItems(fillTable(leftLabel.getText()));
            }
            pb.progressProperty().unbind();
            pb.setProgress(0);
            pi.progressProperty().unbind();
            pi.setProgress(0);
            btnCopy.setDisable(false);
            btnMove.setDisable(false);
            btnDelete.setDisable(false);
            //  btnCancel.setDisable(true);
            btnCancel.setText(btnCancelClose);
            btnCancel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    pb.setVisible(false);
                    pi.setVisible(false);
                    btnCancel.setVisible(false);
                }
            });
        });

        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Wszedlem");
                thread.interrupt();
            }
        });
    }

    public void btnMoveClicked(ActionEvent event) throws IOException {


        btnCancel.setDisable(false);
        btnCancel.setText(btnCancelText);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                HashMap<String, List<String>> myMap = moveFile();
                List<String> paths = new ArrayList<>();
                String destPath="";
                for(Map.Entry<String, List<String>> entry : myMap.entrySet()){
                    if(entry.getKey().equals("paths")){
                        paths = entry.getValue();
                    }
                    else destPath = entry.getValue().get(0);
                }
                try {
                   // moveFile();
                    ListFilesAndDirectories.MoveFile2(paths, destPath);
                    Thread.sleep(1);
                    updateProgress(10, 10);
                } catch (InterruptedException e) {
                    System.out.println("Złapałem");
                    this.cancel(true);
                }
                return null;
            }
        };


        boolean fileExists = false;
        List<String> paths = new ArrayList<>();
        List<String> rootPaths = new ArrayList<>();
        ObservableList<FileInfo> selectedItems = leftTable.getSelectionModel().getSelectedItems();
        for (FileInfo row : selectedItems) {
            paths.add(rightLabel.getText() + "\\" + row.getName());
            rootPaths.add(leftLabel.getText() + "\\" + row.getName());
        }
        for (FileInfo row : selectedItems) {
            String destPath2 = rightLabel.getText() + "\\" + row.getName();
            File testFile = new File(destPath2);
            if (testFile.exists()) {
                if (!fileExists) {
                    fileExists = true;
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, moveAlertMessage, button_tak, button_nie);
                    alert.setHeaderText(null);
                    alert.setTitle(headerTitle);
                    alert.initModality(Modality.WINDOW_MODAL);
                    alert.showAndWait();
                    if (alert.getResult() == button_tak) {
                        Thread thread = new Thread(task);
                        moveTaskSettings(rootPaths, paths, thread, task, false);
                        thread.start();

                    }
                }
            } else if (!testFile.exists() && !fileExists) {
                fileExists = true;
                Thread thread = new Thread(task);
                moveTaskSettings(rootPaths, paths, thread, task, true);
                thread.start();


            }
        }

    }


    public HashMap<String, List<String>> moveFile() throws InterruptedException {

        HashMap<String, List<String>> myMap = new HashMap<>();
        String destPath = rightLabel.getText();
        List<String> paths = new ArrayList<>();
        ObservableList<FileInfo> selectedItems = leftTable.getSelectionModel().getSelectedItems();
        for (FileInfo row : selectedItems) {
            paths.add(leftLabel.getText() + "\\" + row.getName());
        }
        List<String> destPathList = new ArrayList<>();
        destPathList.add(destPath);
        myMap.put("paths", paths);
        myMap.put("destPath", destPathList);

//        ListFilesAndDirectories.MoveFile2(paths, destPath);
        //  ObservableList<FileInfo> list = fillTable(destPath);
        //  rightTable.setItems(list);
        //  leftTable.setItems(fillTable(leftLabel.getText()));
        return myMap;


    }


    public void btnDeleteClicked(ActionEvent event) throws InterruptedException {

        //
//        List<String> paths = new ArrayList<>();
//        ObservableList<FileInfo> selectedItems = leftTable.getSelectionModel().getSelectedItems();
//        for(FileInfo row : selectedItems){
//            paths.add(leftLabel.getText() + "\\"+ row.getName());
//        }
//        ListFilesAndDirectories.DeleteFile2(paths);
        //

        FileInfo leftObj = (FileInfo) leftTable.getSelectionModel().getSelectedItem();
        FileInfo rightObj = (FileInfo) rightTable.getSelectionModel().getSelectedItem();
        boolean leftIsEmpty = leftTable.getSelectionModel().isEmpty();
        boolean rightIsEmpty = rightTable.getSelectionModel().isEmpty();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, localeAlertMessage, button_tak, button_nie);
        alert.setTitle(headerTitle);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setHeaderText(null);
        alert.showAndWait();
        if (alert.getResult() == button_tak) {
            if (!leftIsEmpty) {
                List<String> paths = new ArrayList<>();
                ObservableList<FileInfo> selectedItems = leftTable.getSelectionModel().getSelectedItems();
                for (FileInfo row : selectedItems) {
                    paths.add(leftLabel.getText() + "\\" + row.getName());
                }
                ListFilesAndDirectories.DeleteFile2(paths);
                leftTable.setItems(fillTable(leftLabel.getText()));
            }
            if (!rightIsEmpty) {
                List<String> paths = new ArrayList<>();
                ObservableList<FileInfo> selectedItems = rightTable.getSelectionModel().getSelectedItems();
                for (FileInfo row : selectedItems) {
                    paths.add(rightLabel.getText() + "\\" + row.getName());
                }
                ListFilesAndDirectories.DeleteFile2(paths);
                rightTable.setItems(fillTable(rightLabel.getText()));
            }
        }
    }

    public boolean checkIfExists(String destPath) {
        boolean status = false;
        List<String> destPaths = new ArrayList<>();
        ObservableList<FileInfo> selectedItems = leftTable.getSelectionModel().getSelectedItems();
        for (FileInfo row : selectedItems) {
            destPaths.add(destPath + "\\" + row.getName());
        }
        for (String var : destPaths) {
            File f = new File(var);
            if (f.exists()) {
                status = true;
            }
        }
        return status;
    }

    private void loadLang(String lang) {
        locale = new Locale(lang);
        bundle = ResourceBundle.getBundle("org.kusmp.commander.lang", locale);
        btnSelect1.setText(bundle.getString("btnSelect1"));
        btnSelect2.setText(bundle.getString("btnSelect2"));
        btnCopy.setText(bundle.getString("btnCopy"));
        btnMove.setText(bundle.getString("btnMove"));
        btnDelete.setText(bundle.getString("btnDelete"));
        btnCancel.setText((bundle.getString("btnCancel")));
        leftName.setText(bundle.getString("leftName"));
        leftSize.setText(bundle.getString("leftSize"));
        leftModified.setText(bundle.getString("leftModified"));
        rightName.setText(bundle.getString("rightName"));
        rightSize.setText(bundle.getString("rightSize"));
        rightModified.setText(bundle.getString("rightModified"));
        localeAlertMessage = bundle.getString("localeAlertMessage");
        headerTitle = bundle.getString("deleteAlertTitle");
        moveAlertMessage = bundle.getString("moveAlertMessage");
        btnCancelClose = bundle.getString("btnCancelClose");
        btnCancelText = bundle.getString("btnCancelText");
        piDoneText = bundle.getString(("piDoneText"));

    }

    public void progressBar() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("progressBarLayout.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 358, 159);
            Stage stage = new Stage();
            stage.setTitle("Kopiowanie");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }


}