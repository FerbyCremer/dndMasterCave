package com.assets;

import com.chat.LaunchListener;
import com.chat.Listener;
import com.chat.util.ResizeHelper;
import com.user.MainLaunch;
import com.user.campaignController;
import com.user.playController;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.css.converter.FontConverter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.beans.value.ChangeListener;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
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

    @FXML ToggleButton bold;
    @FXML ToggleButton italic;

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

    Font font = new Font("Garamond", 12);

    private static mainEditController instance;

    public mainEditController() { instance = this; }

    public static mainEditController getInstance() { return instance; }

    //https://stackoverflow.com/questions/26690247/how-to-make-directories-expandable-in-javafx-treeview
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        scale = 30;
        width = 1014;
        height = 650;

        //board.setFocusTraversable(true);
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

        ResourceLibrary.setCellFactory(new Callback<>() {

            public TreeCell<File> call(TreeView<File> tv) {
                return new TreeCell<>() {

                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty == false){
                            setText(item.getName());
                            if (item.isDirectory()) {
                                setGraphic(rootIcon);
                                if (item.getName().contains("Script")) {
                                    setGraphic(scriptIcon);
                                }
                                else if (item.getName().contains("Characters")) {
                                    setGraphic(npcIcon);
                                }
                            } else {
                                setGraphic(leafIcon);
                            }
                        }
                        else {
                            setText("");
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        // Add mouse event handlers for the source
        ResourceLibrary.setOnDragDetected(event -> {
                dragDetected(event);
        });

        ResourceLibrary.setOnDragDone(event -> {
                dragDone(event);
        });

        // Add mouse event handlers for the target
        board.setOnDragOver(event -> {
                dragOver(event);
        });

        board.setOnDragDropped(event -> {
                dragDropped(event);
        });

        //board.widthProperty().bind(workSpace.widthProperty());
        //board.heightProperty().bind(workSpace.heightProperty());
    }

    public void init(){

        hLineCount = (int) Math.floor((height + 1) / scale);
        vLineCount = (int) Math.floor((width + 1) / scale);

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
        if (bold.isSelected()) {
            gc.setFont(Font.font(font.getName(), FontWeight.BOLD, font.getSize()));
            font = gc.getFont();
        }
        else {
            gc.setFont(Font.font(font.getName(), FontWeight.NORMAL, font.getSize()));
            font = gc.getFont();
        }
    }
    @FXML public void setItalic(){
        if (italic.isSelected()) {
            gc.setFont(Font.font(font.getName(), FontPosture.ITALIC, font.getSize()));
            font = gc.getFont();
        }
        else {
            gc.setFont(Font.font(font.getName(), FontPosture.REGULAR, font.getSize()));
            font = gc.getFont();
        }
    }
    @FXML public void setFontSize(){
        fontSize.setOnKeyTyped(event ->  {
            gc.setFont(Font.font(font.getName(), Integer.parseInt(fontSize.getText())));
            System.out.println("enter");
            font = gc.getFont();
        });
    }

    private void dragDetected(MouseEvent event)
    {
        // User can drag only when there is text in the source field
        File sourceText = ResourceLibrary.getSelectionModel().getSelectedItem().getValue();

        // Initiate a drag-and-drop gesture
        Dragboard dragboard = ResourceLibrary.startDragAndDrop(TransferMode.ANY);
        Image image;
        try {
            FileInputStream fis = new FileInputStream(sourceText);
            image = new Image(fis);
        } catch (FileNotFoundException e){
            event.consume();
            return;
        }

        // Add the source text to the Dragboard
        ClipboardContent content = new ClipboardContent();
        content.putImage(image);
        dragboard.setContent(content);
        event.consume();
    }

    private void dragOver(DragEvent event)
    {
        // If drag board has a string, let the event know that
        // the target accepts copy and move transfer modes
        Dragboard dragboard = event.getDragboard();

        if (dragboard.hasImage())
            event.acceptTransferModes(TransferMode.ANY);

        event.consume();
    }

    private void dragDropped(DragEvent event)
    {

        // Transfer the data to the target
        Dragboard dragboard = event.getDragboard();

        if (dragboard.hasImage())
        {
            Image src = dragboard.getImage();
                gc.drawImage(src, event.getX() - src.getWidth()/2, event.getY() - src.getHeight()/2);

            // Data transfer is successful
            event.setDropCompleted(true);
        }
        else
        {
            // Data transfer is not successful
            event.setDropCompleted(false);
        }
        event.consume();
    }

    private void dragDone(DragEvent event)
    {
        // Check how data was transfered to the target. If it was moved, clear the text in the source.
        TransferMode modeUsed = event.getTransferMode();

        if (modeUsed == TransferMode.MOVE)
        { }

        event.consume();
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

    public void loadImage() {
        FileChooser chooser = new FileChooser();

        File map = chooser.showOpenDialog(board.getScene().getWindow());

        File directory = new File("resourceDirs/Images" + File.separator + map.getName());
        try {
            System.out.println(directory.getCanonicalPath());
            Files.copy(map.toPath(), directory.toPath(), StandardCopyOption.REPLACE_EXISTING);
            WritableImage writableImage = new WritableImage((int) board.getWidth(), (int) board.getHeight());
            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);
            board.snapshot(sp, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(renderedImage, ".png", map);
        } catch (IOException e){
            e.printStackTrace();
        }
        findFiles(ResourceLibrary.getRoot().getValue(), null);
        ResourceLibrary.refresh();
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

        parentMenu.getItems().add(butt);
        parentMenu.getItems().add(sqr);
        parentMenu.getItems().add(round);

        // Add MenuItem to ContextMenu
        menu.getItems().addAll(menuItem, parentMenu);

        LineBtn.setContextMenu(menu);
        EraseBtn.setContextMenu(menu);
    }

    public void drawText(MouseEvent event) {
        //board.requestFocus();
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (this.Mode == 0) {
                TextInputDialog dialog = new TextInputDialog("Map Label");
                dialog.setTitle("Labeling");
                dialog.setHeaderText("Enter a map label:");

                Optional<String> result = dialog.showAndWait();

                result.ifPresent(str -> {
                    gc.fillText(str, (int) event.getX(), (int)event.getY());
                });

            } else if (this.Mode == 1) {
                TextInputDialog dialog = new TextInputDialog("Map Notes");
                dialog.setTitle("Labeling");
                dialog.setHeaderText("Enter a map label:");

                Optional<String> result = dialog.showAndWait();

                result.ifPresent(str -> {
                    Tooltip note = new Tooltip(str);
                    Tooltip.install(((Node)event.getSource()), note);
                });
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
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    //System.out.println("directory:" + file.getCanonicalPath());

                    findFiles(file, root);
                } else {
                    //System.out.println("     file:" + file.getCanonicalPath());
                    root.getChildren().add(new TreeItem<File>(file));
                }
            }
            if (parent == null) {
                ResourceLibrary.setRoot(root);
            } else {
                parent.getChildren().add(root);
            }

    }

    @FXML public void clearAll(){
        gc.clearRect(0, 0, width, height);
        init();
    }

    @FXML public void saveMap(){
        FileChooser chooser = new FileChooser();
        File directory = new File("resourceDirs/Maps");
        chooser.setInitialDirectory(directory);

        File map = chooser.showSaveDialog(board.getScene().getWindow());

        try {
            //System.out.println(directory.getCanonicalPath());
            WritableImage writableImage = new WritableImage((int) board.getWidth(), (int) board.getHeight());
            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);
            board.snapshot(sp, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(renderedImage, "PNG", map);
        } catch (IOException e){
            e.printStackTrace();
        }
        findFiles(ResourceLibrary.getRoot().getValue(), null);

        ResourceLibrary.refresh();

    }

    @FXML public void launchCampaign(ActionEvent actionEvent) throws IOException {
        LaunchListener listener;
        Parent window;
        FXMLLoader fmxlLoader = new FXMLLoader(getClass().getClassLoader().getResource("views/LiveCampaign.fxml"));
        window = (BorderPane) fmxlLoader.load();
        dmControl = fmxlLoader.<campaignController>getController();
        listener = new LaunchListener(host, port, user, avatar, dmControl);

        Thread x = new Thread(listener);
        x.setDaemon(true);
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

    /* Terminates Application */
    public void closeSystem(){
        Platform.exit();
        System.exit(0);
    }

    public void minimizeWindow(){ MainLaunch.getPrimaryStage().setIconified(true);
    }
}
