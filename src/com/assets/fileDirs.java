package com.assets;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class fileDirs extends TreeItem {
    private final Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("icons/folder_1.png")));
    private final Node leafIcon = new ImageView(new Image(getClass().getResourceAsStream("icons/file_1.png")));

    private final Node scriptIcon = new ImageView(new Image(getClass().getResourceAsStream("icons/script_1.png")));
    private final Node scriptLeafIcon = new ImageView(new Image(getClass().getResourceAsStream("icons/script_2.png")));

    private final Node npcIcon = new ImageView(new Image(getClass().getResourceAsStream("icons/npc.png")));
    private final Node monsterIcon = new ImageView(new Image(getClass().getResourceAsStream("icons/monster.png")));

    public File mapdir = new File ("resources/maps");
    public File imgDir = new File ("resources/images");
    /* public File backgrnddir = new File (imgDir+"backgrounds");
     public File texdir = new File (imgDir+"textures");
     public File scenarydir = new File (imgDir+"scenary");*/
    public File Scriptdir = new File ("resources/scripts");

    public File lgImgDir = new File ("resources/characters/LgImgs");
    public File smImgDir = new File ("resources/characters/SmImgs");
    public File npcDir = new File ("resources/characters/npcs");
    public File monsterDir = new File ("resources/characters/monsters");

    private TreeItem<File> rootNode = new TreeItem<>();

    public fileDirs() {
        TreeItem<File> maps = new TreeItem<File>(mapdir, rootIcon);
        for (File map : mapdir.listFiles()) {
            TreeItem<File> mapLeaf = new TreeItem<File>(map, leafIcon);
            maps.getChildren().add(mapLeaf);
        }

        TreeItem<File> imgs = new TreeItem<File>(imgDir, rootIcon);
        for (File folder : imgDir.listFiles()) {
            TreeItem<File> leafDir = new TreeItem<File>(folder, rootIcon);
            imgs.getChildren().add(leafDir);
            for (File leaf : folder.listFiles()) {
                TreeItem<File> leafthg = new TreeItem<File>(leaf, leafIcon);
                leafDir.getChildren().add(leafthg);
            }
        }

        TreeItem<File> Script = new TreeItem<File> (Scriptdir, scriptIcon);
        for(File leaf : Scriptdir.listFiles()) {
            TreeItem<File> scriptLeaf = new TreeItem<File>(leaf, scriptLeafIcon);
            Script.getChildren().add(scriptLeaf);
        }


        // ========= NPC Stuff ========
        TreeItem<File> lgImg = new TreeItem<File>(lgImgDir, rootIcon);
        for (File img : lgImgDir.listFiles()) {
            TreeItem<File> leaf = new TreeItem<File>(img, leafIcon);
            lgImg.getChildren().add(leaf);
        }

        TreeItem<File> smImg = new TreeItem<File>(smImgDir, rootIcon);
        for (File img : smImgDir.listFiles()) {
            TreeItem<File> leaf = new TreeItem<File>(img, leafIcon);
            smImg.getChildren().add(leaf);
        }

        TreeItem<File> npcImg = new TreeItem<File>(npcDir, rootIcon);
        for (File img : npcDir.listFiles()) {
            TreeItem<File> leaf = new TreeItem<File>(img, leafIcon);
            npcImg.getChildren().add(leaf);
        }

        TreeItem<File> monsterImg = new TreeItem<File>(monsterDir, rootIcon);
        for (File img : monsterDir.listFiles()) {
            TreeItem<File> leaf = new TreeItem<File>(img, leafIcon);
            monsterImg.getChildren().add(leaf);
        }

        rootNode.getChildren().addAll(maps, imgs, Script, lgImg, smImg, npcImg, monsterImg);
    }

    public TreeItem getRootNode() {
        return rootNode;
    }
}
