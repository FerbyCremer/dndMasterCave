package com.assets;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.TreeView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainEditController implements Initializable {
    @FXML public SubScene mainSpace;
    @FXML public TreeView ResourceLibrary;

    protected Parent mapEditor;
    protected Parent npcEditor;
    private static mainEditController instance;

    public mainEditController() {
        instance = this;
        try {
            instance.setup();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static mainEditController getInstance() { return instance; }

    private void setup() throws IOException{
        mapEditor = FXMLLoader.load(getClass().getClassLoader().getResource("views/MapEditor.fxml"));
        npcEditor = FXMLLoader.load(getClass().getClassLoader().getResource("views/AssetBuilder.fxml"));

        mainSpace.setRoot(mapEditor);

        fileDirs root = new fileDirs();
        ResourceLibrary.setRoot(root.getRootNode());
    }

    @FXML public void loadMapEditor(ActionEvent actionEvent) {
        mainSpace.setRoot(mapEditor);
    }

    @FXML public void loadNPCeditor(ActionEvent actionEvent) {
        mainSpace.setRoot(npcEditor);
    }

    @FXML public void launchCampaign(ActionEvent actionEvent) {
    }

    @FXML public void EditTheme(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

    }
}
