package com.chenzp;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Created by chenzhongpu on 3/18/16.
 */
public class FollowerServerProtocol extends ServerProtocol {

    private ServerBean leaderBean;

    public FollowerServerProtocol(ServerBean leaderBean) {
        super();
        this.leaderBean = leaderBean;
    }

    private String forwardMsgToLeader(String messageId, String clintId, String lockKey, int msgType) {

        try {

            ClientMsg msg = new ClientMsg(msgType, lockKey, clintId);

            String result = SystemUtil.sendTCPMsg(gson.toJson(msg), SystemUtil.LOCALADDRESS,
                    leaderBean.port, false);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String handleClientApply(String messageId, String clientId, String lockKey) {
        // forward the message to leader server
        return forwardMsgToLeader(messageId, clientId, lockKey, Message.APPLY);

    }

    @Override
    public String handleClientRelase(String messageId, String clientId, String lockKey) {

        return forwardMsgToLeader(messageId, clientId, lockKey, Message.RELEASE);
    }

    @Override
    public String processInput(String input) {
        ClientMsg msg = gson.fromJson(input, ClientMsg.class);
        String result;
        System.out.println("processinput = " + msg);
        System.out.println("msg type = " + msg.getMessageType());
        switch (msg.getMessageType()) {
            case Message.CHECKISOWN:
                result = handleClientCheckOwn(msg.getMessageId(), msg.getClientId(),
                        (String)msg.getMessageContent());
                break;
            case Message.APPLY:
                result = handleClientApply(msg.getMessageId(), msg.getClientId(),
                        (String)msg.getMessageContent());
                break;
            case Message.BROADCAST:
                System.out.println("follow get the broadcast..." + msg.getMessageContent());
                Server.lockMap.clear();
                Type type = new TypeToken<ConcurrentHashMap<String, String>>(){}.getType();
                Server.lockMap = gson.fromJson((String)msg.getMessageContent(), type);
                result = null;
                break;
            case Message.RELEASE:
                result = handleClientRelase(msg.getMessageId(), msg.getClientId(),
                        (String)msg.getMessageContent());
                break;
            default:
                result = "";

        }
        return result;
    }
}