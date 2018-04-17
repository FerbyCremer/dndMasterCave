package com.assets;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class mapEditController implements Initializable {
    //protected mapObj world = new mapObj();
    @FXML
    public Canvas board;
    @FXML
    TextField scaleFactor;
    @FXML TextField yVal;
    @FXML TextField xVal;
    @FXML
    Button TextBtn;
    @FXML Button NoteBtn;
    @FXML Button LineBtn;
    @FXML
    ColorPicker paint;

    private int Mode = 0;
    private static mapEditController instance;
    public static mapEditController getInstance() { return instance; }

   public mapEditController(){ instance = this; }

    @FXML public void setBold(){

    }
    @FXML public void setItalic(){

    }
    @FXML public void setUnderline(){

    }
    @FXML public void DragAndDrop(MouseEvent event){
        //TreeItem target = (TreeItem) event.getSource();
        //Dragboard db = event.StartDragAndDrop(TransferMode.MOVE);
        //ClipboardContent content = new ClipboardContent();
        //// Store node ID in order to know what is dragged.
        //content.putString(target.getId());
        //db.setContent(content);
        //event.consume();
    }
    public void changeScale(){
        scaleFactor.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)
                    ((mapObj)board).scale = Double.parseDouble(scaleFactor.getText());
            }
        });
        //repaint?
    }

    public void changeHeight(){
        //mapCanvas.height
    }

    public void changeWidth(){
        // mapCanvas.width = xVal.getValue();
    }

    public void addAsset(){
        // mapFileView.startDragAndDrop(TransferMode.ANY)
        //);
    }
    private double snap(double y) {
        return ((int) y) + 0.5;
    }



    @Override public void initialize(URL location, ResourceBundle resources){
        board  = new mapObj();
        TextBtn.setOnMouseClicked(event -> {
            this.Mode = 0;
        });
        NoteBtn.setOnMouseClicked(event -> {
            this.Mode = 1;
        });
        LineBtn.setOnMouseClicked(event -> {
            this.Mode = 2;
        });
        ((mapObj)board).gc.setFill(paint.getValue());
        ((mapObj)board).gc.setStroke(paint.getValue());
    }

    @FXML public void resize(){
       // ((mapObj)board).resizeMap(xVal.getText(), yVal.getText(), scaleFactor.getText());
    }

    @FXML public void draw(MouseEvent event){
        if(event.getEventType() == MouseEvent.MOUSE_CLICKED){
            if(this.Mode == 0){
                TextInputDialog dialog = new TextInputDialog("Map Label");
                String str = dialog.getResult();
                ((mapObj)board).gc.strokeText(str, (int) event.getX(), (int)event.getY());
            }
            else if(this.Mode == 1){
                TextInputDialog dialog = new TextInputDialog("Map Notes");
                String str = dialog.getResult();
                Tooltip note = new Tooltip(str);
                // Tooltip.install(((Node)event.getSource(), note);
            }
        }
        else if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if(this.Mode == 2) {
                ((mapObj)board).gc.beginPath();
                ((mapObj)board).gc.moveTo(event.getX(), event.getY());
                ((mapObj)board).gc.stroke();
            }
        }
        else if(event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            if(this.Mode == 2) {
                ((mapObj)board).gc.lineTo(event.getX(), event.getY());
                ((mapObj)board).gc.stroke();
                ((mapObj)board).gc.closePath();
                ((mapObj)board).gc.beginPath();
                ((mapObj)board).gc.moveTo(event.getX(), event.getY());
            }
        }
        else if(event.getEventType() == MouseEvent.MOUSE_RELEASED){
            if(this.Mode == 2) {
                ((mapObj)board).gc.lineTo(event.getX(), event.getY());
                ((mapObj)board).gc.stroke();
                ((mapObj)board).gc.closePath();
            }
        }
    }
}
