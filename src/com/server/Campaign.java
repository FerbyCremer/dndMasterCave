package com.server;

import javafx.scene.image.Image;

import java.io.Serializable;

public class Campaign implements Serializable{
    public Image getMap() {
        return map;
    }

    public void setMap(Image map) {
        this.map = map;
    }

    //TODO add initiative list for saving

    Image map;
}
