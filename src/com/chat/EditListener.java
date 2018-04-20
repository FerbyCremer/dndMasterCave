package com.chat;

import com.assets.mainEditController;
import com.chat.messages.Message;
import com.chat.messages.MessageType;
import com.chat.messages.Status;
import com.user.LoginController;
import com.user.campaignController;
import com.user.playController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

import static com.chat.messages.MessageType.CONNECTED;

public class EditListener implements Runnable{

    private static final String HASCONNECTED = "has connected";

    private static String picture;
    private Socket socket;
    public String hostname;
    public int port;
    public static String username;
    public campaignController ccontroller;
    public mainEditController controller;
    private static ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;

    private static EditListener instance;

    public static EditListener getInstance() { return instance; }

    Logger logger = LoggerFactory.getLogger(EditListener.class);

    public EditListener(String hostname, int port, String username, String picture, campaignController controller) {
        this.hostname = hostname;
        this.port = port;
        EditListener.username = username;
        EditListener.picture = picture;
        this.ccontroller = controller;
        instance = this;
    }

    public EditListener(String hostname, int port, String username, String picture, mainEditController controller) {
        this.hostname = hostname;
        this.port = port;
        EditListener.username = username;
        EditListener.picture = picture;
        this.controller = controller;
        instance = this;
        this.controller.host = hostname;
        this.controller.port = port;
        this.controller.user = username;
        this.controller.avatar = picture;
    }

    public void run() {
        try {
            //socket = new Socket(hostname, port);
            LoginController.getInstance().showScene();
            /*outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);*/
        } catch (IOException e) {
           /* mainEditController.getInstance().showErrorDialog("Could not connect to com.server");
            logger.error("Could not Connect");*/
        }
//        logger.info("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());

    }

    /* This method is used for sending a normal Message
     * @param msg - The message which the user generates
     */
    public static void send(String msg) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.USER);
        createMessage.setStatus(Status.AWAY);
        createMessage.setMsg(msg);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }

    //TODO================= SENDING MAPS TO SERVER AND THEN DISTRIBUTED??? ======================
    public static void sendImg(File map) throws IOException {
        File world = map;
        oos.writeObject(world);
        oos.flush();
    }

    /* This method is used for sending a voice Message
     * @param msg - The message which the user generates
     */
    public static void sendVoiceMessage(byte[] audio) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.VOICE);
        createMessage.setStatus(Status.AWAY);
        createMessage.setVoiceMsg(audio);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }

    /* This method is used for sending a normal Message
     * @param msg - The message which the user generates
     */
    public static void sendStatusUpdate(Status status) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.STATUS);
        createMessage.setStatus(status);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }

    /* This method is used to send a connecting message */
    public static void connect() throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(CONNECTED);
        createMessage.setMsg(HASCONNECTED);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
    }

}

