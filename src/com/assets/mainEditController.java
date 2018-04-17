package com.assets;

import com.chat.LaunchListener;
import com.chat.util.ResizeHelper;
import com.user.MainLaunch;
import com.user.campaignController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.beans.value.ChangeListener;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainEditController implements Initializable {

    private final Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folder_1.png")));
    private final Node leafIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/file_1.png")));
    private final Node scriptIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/script_1.png")));
    private final Node npcIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/npc.png")));

    @FXML public TreeView<File> ResourceLibrary;
    @FXML public Canvas board;
    public GraphicsContext gc;
    public GraphicsContext grid;

    @FXML TextField scaleFactor;
    @FXML TextField yVal;
    @FXML TextField xVal;
    @FXML TextField fontSize;
    @FXML Button TextBtn;
    @FXML Button NoteBtn;
    @FXML Button LineBtn;
    @FXML Button EraseBtn;
    @FXML ColorPicker paint;

    public int scale;
    public int width;
    public int height;

    protected int hLineCount;
    protected int vLineCount;

    public static campaignController dmControl;

    private int Mode = 0;

    public String host;
    public int port;
    public String user;
    public String avatar;
    private Scene scene;

    Font font = new Font(12);

    private static mainEditController instance;

    public mainEditController() { instance = this; }

    public static mainEditController getInstance() { return instance; }

//https://stackoverflow.com/questions/26690247/how-to-make-directories-expandable-in-javafx-treeview
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        scale = 30;
        width = 1014;
        height = 650;

        board.setFocusTraversable(true);
        gc = board.getGraphicsContext2D();
        grid = board.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);

        // ------- init file tree ----------
        File currentDir = new File("resourceDirs");
        findFiles(currentDir,null);

        //init map area
        init();

        lineOptions();

        //board.widthProperty().bind(workSpace.widthProperty());
        //board.heightProperty().bind(workSpace.heightProperty());

    }

    public void init(){
        //gc = getGraphicsContext2D();

        hLineCount = (int) Math.floor((height + 1) / scale);
        vLineCount = (int) Math.floor((width + 1) / scale);
        //grid = board.getGraphicsContext2D();

        grid.setFill(Color.WHITE);
        grid.fillRect(0,0, width, height);
        grid.setLineWidth(1);
        grid.setStroke(Color.GRAY);

        for (int i = 0; i < this.hLineCount; i++) {
            grid.strokeLine(0, snap((i + 1) * this.scale), this.width, snap((i + 1) * this.scale));
        }

        for (int i = 0; i < this.vLineCount; i++) {
            grid.strokeLine(snap((i + 1) * this.scale), 0, snap((i + 1) * this.scale), this.height);
        }
    }

    @FXML public void setBold(){
        gc.setFont(Font.font(font.getFamily(), FontWeight.BOLD, font.getSize()));
        font.equals(gc.getFont());
    }
    @FXML public void setItalic(){
        gc.setFont(Font.font(font.getFamily(), FontPosture.ITALIC, font.getSize()));
        font.equals(gc.getFont());
    }
    @FXML public void setFontSize(){
        fontSize.setOnKeyTyped(event ->  {
            gc.setFont(Font.font(font.getFamily(), Integer.parseInt(fontSize.getText())));
            System.out.println("enter");
            font.equals(gc.getFont());
        });
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
    @FXML public void setPaint() {
        gc.setFill(paint.getValue());
        gc.setStroke(paint.getValue());
    }
    @FXML public void changeScale(){
        scaleFactor.setOnKeyTyped(event ->  {
                    resize();
                    System.out.println("enter");
        });
    }
    @FXML public void changeHeight() {
        yVal.setOnKeyTyped(event -> {
            resize();
        });
    }
    @FXML public void changeWidth(){
         xVal.setOnKeyTyped(event ->  {
                 resize();
         });
    }

    public void addAsset(){
        // mapFileView.startDragAndDrop(TransferMode.ANY)
        //);
    }
    private double snap(double y) {
        return ((int) y) + 0.5;
    }

    private void resize(){
        scale = Integer.parseInt(scaleFactor.getText());
        width = Integer.parseInt(xVal.getText());
        height = Integer.parseInt(yVal.getText());
        System.out.println(scale);
        init();
        // ((mapObj)board).resize(xVal.getText(), yVal.getText(), scaleFactor.getText());
    }
@FXML public void setLineBtn(){
        this.Mode = 2;
}
@FXML public void setTextBtn(){
        this.Mode = 0;
}
@FXML public void setNoteBtn(){
        this.Mode = 1;
}
@FXML public void setErase(){
        this.Mode = 3;
}

    private void lineOptions(){
        ContextMenu menu = new ContextMenu();

        Menu menuItem = new Menu("Size");

        TextField ptsize = new TextField("5");
        CustomMenuItem input = new CustomMenuItem(ptsize);
        gc.setLineWidth(Double.parseDouble(ptsize.getText()));
        menuItem.getItems().add(input);

    Menu parentMenu = new Menu("Shape");

    RadioMenuItem butt = new RadioMenuItem("butt");
    butt.setOnAction(event -> gc.setLineCap(StrokeLineCap.BUTT));
    RadioMenuItem sqr = new RadioMenuItem("square");
    sqr.setOnAction(event -> gc.setLineCap(StrokeLineCap.SQUARE));
    RadioMenuItem round = new RadioMenuItem("round");
    round.setOnAction(event -> gc.setLineCap(StrokeLineCap.ROUND));
    ToggleGroup group = new ToggleGroup();

    butt.setToggleGroup(group);
    sqr.setToggleGroup(group);
    round.setToggleGroup(group);

    // Add MenuItem to ContextMenu
    menu.getItems().addAll(menuItem, parentMenu);

    LineBtn.setContextMenu(menu);
        EraseBtn.setContextMenu(menu);
}

    public void drawText(MouseEvent event) {
        board.requestFocus();
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (this.Mode == 0) {
                TextInputDialog dialog = new TextInputDialog("Map Label");
                String str = dialog.getResult();
                gc.strokeText(str, (int) event.getX(), (int)event.getY());
            } else if (this.Mode == 1) {
                TextInputDialog dialog = new TextInputDialog("Map Notes");
                String str = dialog.getResult();
                Tooltip note = new Tooltip(str);
                Tooltip.install(((Node)event.getSource()), note);
            }
        }
    }

    @FXML public void drawLine(MouseEvent event){
        if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if(this.Mode == 2) {
                System.out.println("drawing shit");
                gc.beginPath();
                gc.moveTo(event.getX(), event.getY());
                gc.stroke();
            }
        }
        else if(event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            if(this.Mode == 2) {
                gc.lineTo(event.getX(), event.getY());
                gc.stroke();
                gc.closePath();
                gc.beginPath();
                gc.moveTo(event.getX(), event.getY());
            }
            else if (this.Mode == 3) {
                gc.clearRect(event.getX(), event.getY(), gc.getLineWidth(), gc.getLineWidth());
            }
        }
        else if(event.getEventType() == MouseEvent.MOUSE_RELEASED){
            if(this.Mode == 2) {
                gc.lineTo(event.getX(), event.getY());
                gc.stroke();
                gc.closePath();
            }
        }
    }

    private void findFiles(File dir, TreeItem<File> parent){
        TreeItem<File> root = new TreeItem<>(dir);
        root.setGraphic(rootIcon);
        if (root.toString().contains("Script")) {
            root.setGraphic(scriptIcon);
        }
        else if (root.toString().contains("Characters")) {
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
                    root.setGraphic(rootIcon);
                    if (root.toString().contains("Script")) {
                        root.setGraphic(scriptIcon);
                    }
                    else if (root.toString().contains("Characters")) {
                        root.setGraphic(npcIcon);
                    }
                    findFiles(file, root);
                } else {
                    System.out.println("     file:" + file.getCanonicalPath());
                    root.getChildren().add(new TreeItem<>(file, leafIcon));
                }
            }
            if (parent == null) {
                ResourceLibrary.setRoot(root);
            } else {
                parent.getChildren().add(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void clearAll(){
        gc.clearRect(0, 0, width, height);
    }

    @FXML public void saveMap(){

    }

    @FXML public void launchCampaign(ActionEvent actionEvent) throws IOException {
        LaunchListener listener;
        Parent window;
        FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/views/LiveController.fxml"));
        window = (BorderPane) fmxlLoader.load();
        dmControl = fmxlLoader.<campaignController>getController();
        listener = new LaunchListener(host, port, user, avatar, dmControl);

        Thread x = new Thread(listener);
        x.start();
        this.scene = new Scene(window);
    }

    public void showScene() throws IOException {
        Platform.runLater( () -> {
            Stage stage = (Stage) board.getScene().getWindow();
            stage.setResizable(true);
            stage.setWidth(1040);
            stage.setHeight(620);

            stage.setOnCloseRequest((WindowEvent e) -> {
                Platform.exit();
                System.exit(0);
            });
            stage.setScene(this.scene);
            stage.setMinWidth(800);
            stage.setMinHeight(300);
             ResizeHelper.addResizeListener(stage);
            stage.centerOnScreen();
        });
    }

    @FXML public void EditTheme(ActionEvent actionEvent) {
    }

    @FXML public void loadMapEditor(ActionEvent actionEvent) {
       // mainSpace.setRoot(mapEditor);
    }

    @FXML public void loadNPCeditor(ActionEvent actionEvent) {
        //mainSpace.setRoot(npcEditor);
    }


    /* Terminates Application */
    public void closeSystem(){
        Platform.exit();
        System.exit(0);
    }

    public void minimizeWindow(){ MainLaunch.getPrimaryStage().setIconified(true);
    }

    /*private final class TextFieldTreeCellImpl extends TreeCell<String> {

        private TextField textField;

        public TextFieldTreeCellImpl() {
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText((String) getItem());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(textField.getText());
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }*/
}
