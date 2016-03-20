package com.team6.sjtu;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

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

    private String forwardMsgToLeader(String clintId, String lockKey, int msgType) {

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
    public String handleClientApply(String clientId, String lockKey) {
        // forward the message to leader server
        return forwardMsgToLeader(clientId, lockKey, Message.APPLY);

    }

    @Override
    public String handleClientRelase(String clientId, String lockKey) {

        return forwardMsgToLeader(clientId, lockKey, Message.RELEASE);
    }

    @Override
    public String processInput(String input) {
        ClientMsg msg = gson.fromJson(input, ClientMsg.class);
        String result;
        System.out.println("processinput = " + msg);
        System.out.println("msg type = " + msg.getMessageType());
        switch (msg.getMessageType()) {
            case Message.CHECKISOWN:
                result = handleClientCheckOwn(msg.getClientId(),
                        (String)msg.getMessageContent());
                break;
            case Message.APPLY:
                result = handleClientApply(msg.getClientId(),
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
                result = handleClientRelase(msg.getClientId(),
                        (String)msg.getMessageContent());
                break;
            default:
                result = "";

        }
        return result;
    }
}