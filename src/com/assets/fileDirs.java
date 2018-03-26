package com.assets;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;

public class fileDirs extends TreeView {
    private final Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folder_1.png")));
    private final Node leafIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/file_1.png")));

    private final Node scriptIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/script_1.png")));
    private final Node npcIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/npc.png")));

    @FXML
    private TreeView<File> lib;
    public fileDirs() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/customComponents/FileDir.fxml"));
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        File currentDir = new File("resourceDirs");
        findFiles(currentDir, null);

    }

    public void findFiles(File dir, TreeItem<File> parent){
        TreeItem<File> root = new TreeItem<>(dir);
        root.setExpanded(true);
        if (root.toString().contains("Script")) {
            root.setGraphic(scriptIcon);
        }
        else if (root.toString().contains("Character")) {
            root.setGraphic(npcIcon);
        }
        else {
            root.setGraphic(rootIcon);
        }
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println("directory:" + file.getCanonicalPath());
                    findFiles(file, root);
                } else {
                    System.out.println("     file:" + file.getCanonicalPath());
                    root.getChildren().add(new TreeItem<File>(file, leafIcon));
                }
            }
            if (parent == null) {
                lib.setRoot(root);
            } else {
                parent.getChildren().add(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}