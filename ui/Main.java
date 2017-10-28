package ui;

import core.HuffmanCompress;
import core.HuffmanDecompress;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Random;


/**
 * @author 16307110325
 * Created on 2017/10/28.
 */
public class Main extends Application {
    private TextField fileToBeCompressed = new TextField();
    private TextField dirToBeCompressed = new TextField();
    private TextField compressTo = new TextField();
    private TextField fileToBeDecompressed = new TextField();
    private TextField decompressTo = new TextField();
    private Button chooseFileToBeCompressed = new Button("选择文件");
    private Button chooseDirToBecompressed = new Button("选择文件夹");
    private Button chooseToCompressTo = new Button("压缩到");
    private Button startCompress = new Button("   开始压缩\r\n(文件夹优先)");
    private Button chooseFileToBeDecompressed = new Button("选择文件");
    private Button chooseToDecompressTo = new Button("解压到");
    private Button startDecompress = new Button("开始解压");
    private TextArea console1 = new TextArea();
    private TextArea console2 = new TextArea();

    @Override
    public void start(Stage primaryStage) throws Exception {
        TabPane tabPane = new TabPane();

        tabPane.setTabMinWidth(250.0);
        tabPane.setTabMinHeight(30.0);
        tabPane.setPadding(new Insets(10.0, 5.0, 10.0, 5.0));

        Tab tab1 = new Tab("压缩");
        Tab tab2 = new Tab("解压");
        tab1.setClosable(false);
        tab2.setClosable(false);

        tabPane.getTabs().addAll(tab1, tab2);

        GridPane gridPane1 = new GridPane();
        GridPane gridPane2 = new GridPane();

        gridPane1.setHgap(10.0);
        gridPane2.setHgap(10.0);
        gridPane1.setVgap(10.0);
        gridPane2.setVgap(10.0);

        fileToBeDecompressed.setMinWidth(420.0);
        fileToBeCompressed.setMinWidth(420.0);

        gridPane1.add(fileToBeCompressed, 0, 1, 2, 1);
        gridPane1.add(chooseFileToBeCompressed, 2, 1);
        gridPane1.add(dirToBeCompressed, 0, 2, 2, 1);
        gridPane1.add(chooseDirToBecompressed, 2, 2);
        gridPane1.add(compressTo, 0, 3, 2, 1);
        gridPane1.add(chooseToCompressTo, 2, 3);
        gridPane1.add(startCompress, 2, 4);
        gridPane1.add(console1, 0, 5, 3, 5);

        gridPane2.add(fileToBeDecompressed, 0, 1, 2, 1);
        gridPane2.add(chooseFileToBeDecompressed, 2, 1);
        gridPane2.add(decompressTo, 0, 2, 2, 1);
        gridPane2.add(chooseToDecompressTo, 2, 2);
        gridPane2.add(startDecompress, 2, 3);
        gridPane2.add(console2, 0, 4, 3, 5);

        gridPane1.setAlignment(Pos.CENTER);
        gridPane2.setAlignment(Pos.CENTER);

        fileToBeCompressed.setAlignment(Pos.BOTTOM_LEFT);
        fileToBeDecompressed.setAlignment(Pos.BOTTOM_LEFT);
        chooseFileToBeCompressed.setAlignment(Pos.BOTTOM_RIGHT);
        chooseFileToBeDecompressed.setAlignment(Pos.BOTTOM_RIGHT);
        chooseToCompressTo.setAlignment(Pos.BOTTOM_RIGHT);
        chooseToDecompressTo.setAlignment(Pos.BOTTOM_RIGHT);
        compressTo.setAlignment(Pos.BOTTOM_LEFT);
        decompressTo.setAlignment(Pos.BOTTOM_LEFT);
        console1.setEditable(false);
        console2.setEditable(false);
        console1.setWrapText(true);
        console2.setWrapText(true);
        console1.appendText("压缩速度为30~40M/min，点击按钮后请等待\r\n压缩已经压缩过的文件可能会轻微增大");
        console2.appendText("解压速度为 3~5 M/min，点击按钮后请等待\r\n");

        tab1.setContent(gridPane1);
        tab2.setContent(gridPane2);

        startCompress.setOnAction(e -> startCompress());
        startDecompress.setOnAction(e -> startDecompress());
        chooseFileToBeCompressed.setOnAction(e -> chooseCompressFile(primaryStage));
        chooseDirToBecompressed.setOnAction(e -> chooseCompressDir(primaryStage));
        chooseFileToBeDecompressed.setOnAction(e -> chooseDecompressFile((primaryStage)));
        chooseToCompressTo.setOnAction(e -> compressTo(primaryStage));
        chooseToDecompressTo.setOnAction(e -> decompressTo(primaryStage));

        primaryStage.setTitle("ZCompress 16307110325");
        primaryStage.setScene(new Scene(tabPane));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void chooseCompressFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择要压缩的文件");
        File file = fileChooser.showOpenDialog(stage);
        fileToBeCompressed.setText(file.getPath());
    }

    private void chooseCompressDir(Stage stage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择要压缩的文件夹");
        File file = directoryChooser.showDialog(stage);
        dirToBeCompressed.setText(file.getPath());
    }

    private void compressTo(Stage stage) {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("压缩到");
        File file = fileChooser.showDialog(stage);
        compressTo.setText(file.getPath() + "\\" + new Random().nextInt() + ".zcs" );
    }

    private void chooseDecompressFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择要解压的Zcs文件");
        File file = fileChooser.showOpenDialog(stage);
        fileToBeDecompressed.setText(file.getPath());
    }

    private void decompressTo(Stage stage) {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("解压到");
        File file = fileChooser.showDialog(stage);
        decompressTo.setText(file.getPath());
    }

    private void startCompress() {
        String inputPath = dirToBeCompressed.getText().length() == 0 ?
                fileToBeCompressed.getText() : dirToBeCompressed.getText();
        String outputPath = compressTo.getText();
        try {
            File inputFile = new File(inputPath);
            File outputFile = new File(outputPath);
            console1.appendText("压缩 " + inputPath + "中...\r\n");
            if (outputFile.exists()) {
                Alert information = new Alert(Alert.AlertType.INFORMATION,"文件已经存在");
                information.show();
            } else {
                long time = System.currentTimeMillis();
                HuffmanCompress.compress(inputFile, outputFile);
                time = (System.currentTimeMillis() - time) / 1000;
                console1.appendText("压缩完成，已经压缩到 " + outputPath + "\r\n");
                console1.appendText("用时秒数：" + time + "\r\n");
                System.out.println("finish compress");
            }
        } catch (Exception e) {
            Alert information = new Alert(Alert.AlertType.INFORMATION,"读写错误");
            information.show();
        }
    }

    private void startDecompress() {
        String inputPath = fileToBeDecompressed.getText();
        String outputPath = decompressTo.getText();
        console2.appendText("解压 " + inputPath + "中...\r\n");
        try {
            File inputFile = new File(inputPath);
            File outputFile = new File(outputPath);
            long time = System.currentTimeMillis();
            HuffmanDecompress.decompressFile(inputFile, outputFile);
            time = (System.currentTimeMillis() - time) / 1000;
            console2.appendText("解压完成\r\n");
            console2.appendText("用时秒数：" + time + "\r\n");
        } catch (Exception e) {
            Alert information = new Alert(Alert.AlertType.INFORMATION,"文件不合法或读写错误");
            information.show();
        }
    }

}
