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

public class Listener implements Runnable{

    private static final String HASCONNECTED = "has connected";

    private static String picture;
    private Socket socket;
    public String hostname;
    public int port;
    public static String username;
    private static File world = new File("test.png");
    //public chatController controller;
    //public campaignController controller;
    public playController pcontroller;
    public mainEditController mcontroller;
    private static ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;

    private static Listener instance;

    public static Listener getInstance() { return instance; }

    Logger logger = LoggerFactory.getLogger(Listener.class);

/*
    public Listener(String hostname, int port, String username, String picture, campaignController controller) {
        this.hostname = hostname;
        this.port = port;
        Listener.username = username;
        Listener.picture = picture;
        this.controller = controller;
        instance = this;
    }
*/

    public Listener(String hostname, int port, String username, String picture, playController controller) {
        this.hostname = hostname;
        this.port = port;
        Listener.username = username;
        Listener.picture = picture;
        this.pcontroller = controller;
        instance = this;
    }

/*    public Listener(String hostname, int port, String username, String picture, mainEditController controller) {
        this.hostname = hostname;
        this.port = port;
        Listener.username = username;
        Listener.picture = picture;
        this.mcontroller = controller;
        instance = this;
    }*/

    public void run() {
        try {
            socket = new Socket(hostname, port);
            LoginController.getInstance().showScene();
            outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);
        } catch (IOException e) {
            LoginController.getInstance().showErrorDialog("Could not connect to com.server");
            logger.error("Could not Connect");
        }
        logger.info("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());

        try {
            try{
            connect();
            logger.info("Sockets in and out ready!");
            while (socket.isConnected()) {
                Message message = null;
                message = (Message) input.readObject();

                if (message != null) {
                    logger.debug("Message recieved:" + message.getMsg() + " MessageType:" + message.getType() + "Name:" + message.getName());
                    switch (message.getType()) {
                        case USER:
                            pcontroller.addToChat(message);
                            break;
                        case VOICE:
                            pcontroller.addToChat(message);
                            break;
                        case NOTIFICATION:
                            pcontroller.newUserNotification(message);
                            break;
                        case SERVER:
                            pcontroller.addAsServer(message);
                            break;
                        case CONNECTED:
                            pcontroller.setUserList(message);
                            break;
                        case DISCONNECTED:
                            pcontroller.setUserList(message);
                            break;
                        case STATUS:
                            pcontroller.setUserList(message);
                            break;
                        case IMAGE:
                            pcontroller.setMap(message);
                            break;
                    }
                }
            }
            } catch(EOFException e) {
                e.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            pcontroller.logoutScene();
        }
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
        createMessage.setMap(world);
        oos.writeObject(createMessage);
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
//        createMessage.setMap(world);
        oos.writeObject(createMessage);
    }

}

