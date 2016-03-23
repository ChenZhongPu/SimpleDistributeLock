package com.team6.sjtu;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

/**
 * Created by chenzhongpu on 3/15/16.
 *
 * DistributedClient class is client.
 */
public class DistributedClient {

    public String clientId;
    private boolean isConnected;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson;

    /**
     *
     * @param serverAddress the address of server
     */
    public DistributedClient(String serverAddress) {
        this.isConnected = connectToServer(serverAddress);
        if (this.isConnected) {
            this.clientId = getClientId();
            this.gson = new Gson();
        }
    }

    /**
     * @see Message
     * @see ClientMsg
     *
     * @param messageType message type
     * @param messageContent message content
     * @return true if success, false otherwise
     */
    private boolean sendMsg(int messageType, String messageContent) {

        ClientMsg msg = new ClientMsg(messageType, messageContent, clientId);
        out.println(gson.toJson(msg));

        try {
            SimpleMsg echoMsg = gson.fromJson(in.readLine(), SimpleMsg.class);
            return (boolean)echoMsg.getMessageContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * try to get the lock
     *
     * @param lockKey lock key
     * @return true if success, false otherwise
     */
    public boolean tryLock(String lockKey) {

        return sendMsg(Message.APPLY, lockKey);
    }

    /**
     * release the lock
     *
     * @param lockKey lock key
     * @return true if success, false otherwise
     */
    public boolean unLock(String lockKey) {

        return sendMsg(Message.RELEASE, lockKey);
    }

    /**
     * check whether it owns the lock
     *
     * @param lockKey
     * @return true if owns, false otherwise
     */
    public boolean checkIsOwn(String lockKey) {

        return sendMsg(Message.CHECKISOWN, lockKey);
    }

    // connect to server
    private boolean connectToServer(String serverAddress) {
        boolean success = false;
        try {
            socket = new Socket(SystemUtil.LOCALADDRESS, SystemUtil.getDNSMap().get(serverAddress));
            in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
            out =
                    new PrintWriter(socket.getOutputStream(), true);
            out.println(Message.HELLO);
            success =  true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    // generate client id using UUID
    private String getClientId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Client " + clientId;
    }

}


