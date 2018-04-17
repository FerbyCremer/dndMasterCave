package com.user;

import com.assets.mainEditController;
import com.chat.EditListener;
import com.chat.LaunchListener;
import com.chat.Listener;
import com.chat.chatController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private ImageView Defaultview;
    // @FXML private ImageView Sarahview;
    // @FXML private ImageView Dominicview;
    @FXML protected RadioButton role;
    @FXML public TextField hostnameTextfield;
    @FXML private TextField portTextfield;
    @FXML private TextField usernameTextfield;
  //  @FXML private ChoiceBox imagePicker;
    @FXML private Label selectedPicture;
     public static playController campaign;
     public static mainEditController dmControl;
    @FXML private BorderPane frame;
    private double xOffset;
    private double yOffset;
    private Scene scene;

    private static LoginController instance;

    public LoginController() { instance = this; }

    public static LoginController getInstance() { return instance; }

    public void loginButtonAction() throws IOException {
        String host = hostnameTextfield.getText();
        int port = Integer.parseInt(portTextfield.getText());
        String user = usernameTextfield.getText();
        String avatar = selectedPicture.getText();
        Parent window;


        if(role.isSelected()) {
            Listener listener;

            FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/views/mainCampaignView.fxml"));
            window = (BorderPane) fmxlLoader.load();
            campaign = fmxlLoader.<playController>getController();
            listener = new Listener(host, port, user, avatar, campaign);

            Thread x = new Thread(listener);
            x.start();
            //this.scene = new Scene(window);
        } else {
            EditListener listener;

            FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/views/MainEditUI.fxml"));
            window = (BorderPane) fmxlLoader.load();
            dmControl = fmxlLoader.<mainEditController>getController();
            listener = new EditListener(host, port, user, avatar, dmControl);

            Thread x = new Thread(listener);
            x.start();
        }
            this.scene = new Scene(window);
    }

    public void showScene() throws IOException {
        Platform.runLater( () -> {
            Stage stage = (Stage) hostnameTextfield.getScene().getWindow();
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
            stage.centerOnScreen();
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        imagePicker.getSelectionModel().selectFirst();
      //  selectedPicture.textProperty().bind(imagePicker.getSelectionModel().selectedItemProperty());
       // selectedPicture.setVisible(false);

        /* Drag and Drop */
        frame.setOnMousePressed(event -> {
            xOffset = MainLaunch.getPrimaryStage().getX() - event.getScreenX();
            yOffset = MainLaunch.getPrimaryStage().getY() - event.getScreenY();
            frame.setCursor(Cursor.CLOSED_HAND);
        });

        frame.setOnMouseDragged(event -> {
            MainLaunch.getPrimaryStage().setX(event.getScreenX() + xOffset);
            MainLaunch.getPrimaryStage().setY(event.getScreenY() + yOffset);

        });

        frame.setOnMouseReleased(event -> {
            frame.setCursor(Cursor.DEFAULT);
        });

        // imagePicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        //@Override
           /* public void changed(ObservableValue<? extends String> selected, String oldPicture, String newPicture) {
                if (oldPicture != null) {
                    switch (oldPicture) {
                        case "Default":
                            Defaultview.setVisible(false);
                            break;
                        case "Dominic":
                            Dominicview.setVisible(false);
                            break;
                        case "Sarah":
                            Sarahview.setVisible(false);
                            break;
                    }
                }
                if (newPicture != null) {
                    switch (newPicture) {
                        case "Default":
                            Defaultview.setVisible(true);
                            break;
                        case "Dominic":
                            Dominicview.setVisible(true);
                            break;
                        case "Sarah":
                            Sarahview.setVisible(true);
                            break;
                    }
                }
            }*/
        // });
        int numberOfSquares = 30;
        while (numberOfSquares > 0){
            generateAnimation();
            numberOfSquares--;
        }
    }


    /* This method is used to generate the animation on the login window, It will generate random ints to determine
     * the size, speed, starting points and direction of each square.
     */
    public void generateAnimation(){
        Random rand = new Random();
        int sizeOfSqaure = rand.nextInt(50) + 1;
        int speedOfSqaure = rand.nextInt(10) + 5;
        int startXPoint = rand.nextInt(420);
        int startYPoint = rand.nextInt(350);
        int direction = rand.nextInt(5) + 1;

        KeyValue moveXAxis = null;
        KeyValue moveYAxis = null;
        Rectangle r1 = null;

        switch (direction){
            case 1 :
                // MOVE LEFT TO RIGHT
                r1 = new Rectangle(0,startYPoint,sizeOfSqaure,sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 350 -  sizeOfSqaure);
                break;
            case 2 :
                // MOVE TOP TO BOTTOM
                r1 = new Rectangle(startXPoint,0,sizeOfSqaure,sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), 420 - sizeOfSqaure);
                break;
            case 3 :
                // MOVE LEFT TO RIGHT, TOP TO BOTTOM
                r1 = new Rectangle(startXPoint,0,sizeOfSqaure,sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 350 -  sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), 420 - sizeOfSqaure);
                break;
            case 4 :
                // MOVE BOTTOM TO TOP
                r1 = new Rectangle(startXPoint,420-sizeOfSqaure ,sizeOfSqaure,sizeOfSqaure);
                moveYAxis = new KeyValue(r1.xProperty(), 0);
                break;
            case 5 :
                // MOVE RIGHT TO LEFT
                r1 = new Rectangle(420-sizeOfSqaure,startYPoint,sizeOfSqaure,sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 0);
                break;
            case 6 :
                //MOVE RIGHT TO LEFT, BOTTOM TO TOP
                r1 = new Rectangle(startXPoint,0,sizeOfSqaure,sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 350 -  sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), 420 - sizeOfSqaure);
                break;

            default:
                System.out.println("default");
        }

        r1.setFill(Color.web("#F89406"));
        r1.setOpacity(0.1);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(speedOfSqaure * 1000), moveXAxis, moveYAxis);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        frame.getChildren().add(frame.getChildren().size()-1,r1);
    }

    /* Terminates Application */
    public void closeSystem(){
        Platform.exit();
        System.exit(0);
    }

    public void minimizeWindow(){ MainLaunch.getPrimaryStage().setIconified(true);
    }

    /* This displays an alert message to the user */
    public void showErrorDialog(String message) {
        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText(message);
            alert.setContentText("Please check for firewall issues and check if the server is running.");
            alert.showAndWait();
        });

    }
}
