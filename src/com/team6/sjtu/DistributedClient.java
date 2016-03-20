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
 */
public class DistributedClient {
    private String clientId;
    private boolean isConnected;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson;
    private static Map<String, Integer> dnsMap = SystemUtil.getDNSMap();

    public DistributedClient(String serverAddress) {
        this.isConnected = connectToServer(serverAddress);
        if (this.isConnected) {
            this.clientId = getClientId();
            this.gson = new Gson();
        }
    }

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
        try {
            socket = new Socket(SystemUtil.LOCALADDRESS, dnsMap.get(serverAddress));
            in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
            out =
                    new PrintWriter(socket.getOutputStream(), true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private String getClientId() {
        return UUID.randomUUID().toString();
    }


}
