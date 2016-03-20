package com.team6.sjtu;

import com.google.gson.Gson;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

/**
 * Created by chenzhongpu on 3/15/16.
 */
public class DistributedClient {

    public String clientId;
    private boolean isConnected;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson;

    public DistributedClient(String serverAddress) {
        this.isConnected = connectToServer(serverAddress);
        if (this.isConnected) {
            this.clientId = getClientId();
            this.gson = new Gson();
        }
    }

    private boolean sendMsg(int messageType, String messageContent) {

        ClientMsg msg = new ClientMsg(messageType, messageContent, clientId);
        System.out.println("client send msg ..." + msg);

        out.println(gson.toJson(msg));

        try {
            SimpleMsg echoMsg = gson.fromJson(in.readLine(), SimpleMsg.class);
            return (boolean)echoMsg.getMessageContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean tryLock(String lockKey) {

        return sendMsg(Message.APPLY, lockKey);
    }

    public boolean unLock(String lockKey) {

        return sendMsg(Message.RELEASE, lockKey);
    }

    public boolean checkIsOwn(String lockKey) {

        return sendMsg(Message.CHECKISOWN, lockKey);
    }

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

    private String getClientId() {
        return UUID.randomUUID().toString();
    }

}


