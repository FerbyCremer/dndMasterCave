package com.assets;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class mapObj extends Canvas {
    public double scale;

    public int width;
    public int height;

    protected int hLineCount;
    protected int vLineCount;

    public Canvas canvas;

    public GraphicsContext gc;
    private GraphicsContext grid;

    public mapObj(){
        scale = 30;
        width = 720;
        height = 500;

        hLineCount = (int) Math.floor((height + 1) / scale);
        vLineCount = (int) Math.floor((width + 1) / scale);
        canvas = new Canvas((double)width, (double)height);

        gc = canvas.getGraphicsContext2D();
        grid = canvas.getGraphicsContext2D();

        grid.clearRect(0,0, width, height);

        for (int i = 0; i < hLineCount; i++) {
            grid.strokeLine(0, snap((i + 1) * this.scale), width, snap((i + 1) * this.scale));
        }

        for (int i = 0; i < vLineCount; i++) {
            grid.strokeLine(snap((i + 1) * this.scale), 0, snap((i + 1) * this.scale), height);
        }
    }

    public void resizeMap( String w, String h, String s){
        scale = Double.parseDouble(s);
        width = Integer.parseInt(w);
        height = Integer.parseInt(h);

        canvas.resize((double)width, (double)height);

        //grid  = this.getGraphicsContext2D();
        grid.clearRect(0,0, width, height);
        grid.setLineWidth(1);

        for (int i = 0; i < hLineCount; i++) {
            grid.strokeLine(0, snap((i + 1) * this.scale), width, snap((i + 1) * this.scale));
        }

        for (int i = 0; i < vLineCount; i++) {
            grid.strokeLine(snap((i + 1) * this.scale), 0, snap((i + 1) * this.scale), height);
        }
    }

    private double snap(double y) {
        return ((int) y + 0.5);
    }
}
