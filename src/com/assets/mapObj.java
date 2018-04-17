package com.assets;

import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.GREEN;

public class mapObj extends Canvas{
    public double scale;

    public double width;
    public double height;

    protected int hLineCount;
    protected int vLineCount;

    public GraphicsContext gc;
    public GraphicsContext grid;

    private void draw() {
        //gc = getGraphicsContext2D();
        //gc.clearRect(0, 0, width, height);

        grid  = getGraphicsContext2D();
        grid.setFill(Color.WHITE);
        grid.fillRect(0,0, width, height);
        grid.setLineWidth(1);
        grid.setStroke(Color.GRAY);

        for (int i = 0; i < hLineCount; i++) {
            grid.strokeLine(0, snap((i + 1) * this.scale), width, snap((i + 1) * this.scale));
            grid.stroke();
        }

        for (int i = 0; i < vLineCount; i++) {
            grid.strokeLine(snap((i + 1) * this.scale), 0, snap((i + 1) * this.scale), height);
            grid.stroke();
        }
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    public mapObj() {
        super();
        scale = 30;
        width = 1014;
        height = 650;
        hLineCount = (int) Math.floor((height + 1) / scale);
        vLineCount = (int) Math.floor((width + 1) / scale);
    }

    public void init(){
        //gc = getGraphicsContext2D();
        grid = getGraphicsContext2D();

        grid.setFill(GREEN);
        grid.fillRect(0,0, width, height);
        grid.setLineWidth(4);
        grid.setStroke(Color.RED);

        for (int i = 0; i < this.hLineCount; i++) {
            grid.strokeLine(0, snap((i + 1) * this.scale), this.width, snap((i + 1) * this.scale));
        }

        for (int i = 0; i < this.vLineCount; i++) {
            grid.strokeLine(snap((i + 1) * this.scale), 0, snap((i + 1) * this.scale), this.height);
        }

        // Redraw canvas when size changes.
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }
    public void resize( String w, String h, String s){
        scale = Double.parseDouble(s);
        width = Double.parseDouble(w);
        height = Double.parseDouble(h);

        this.draw();
    }

    private double snap(double y) {
        return ((int) y + 0.5);
    }
}
