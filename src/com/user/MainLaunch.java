package com.user;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainLaunch extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/LoginView.fxml"));
//        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/MainEditUI.fxml"));
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("D&D Master Cave");
        //stage.getIcons().add(new Image(getClass().getClassLoader().getResource("icons/logo.png").toString()));
        Scene mainScene = new Scene(root);
        mainScene.setRoot(root);
        stage.setResizable(true);
        stage.setScene(mainScene);
        stage.show();
        stage.setOnCloseRequest( e -> Platform.exit());
    }

    public static void main(String[] args) { launch(args); }

    public static Stage getPrimaryStage() { return primaryStage; }
}
