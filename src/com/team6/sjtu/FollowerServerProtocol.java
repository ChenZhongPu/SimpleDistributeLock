package com.team6.sjtu;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.concurrent.ConcurrentHashMap;
/**
 * Created by chenzhongpu on 3/18/16.
 *
 * This class inherits from ServerProtocol,
 * for handling the message to Leader Server.
 *
 * @see ServerProtocol
 *
 */
public class FollowerServerProtocol extends ServerProtocol {

    private ServerBean leaderBean;

    /**
     * @see ServerBean
     * @param leaderBean the leader server
     */
    public FollowerServerProtocol(ServerBean leaderBean) {
        super();
        this.leaderBean = leaderBean;
    }

    /**
     * forward the apply lock or release lock message to leader server
     * @see Message
     * @see ClientMsg
     * @see SimpleMsg
     * @param clintId an UUID string represents client ID
     * @param lockKey the key of a lock
     * @param msgType the type of message
     * @return the json message of SimpleMsg
     */
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


    /**
     * process the message of applying the lock.
     * it will forward to leader.
     * @param clientId an UUID string represents client ID
     * @param lockKey the key of a lock
     * @return the json message of SimpleMsg
     */
    @Override
    public String handleClientApply(String clientId, String lockKey) {
        // forward the message to leader server
        return forwardMsgToLeader(clientId, lockKey, Message.APPLY);

    }

    /**
     * process the message of releasing the lock.
     * it will forward to leader.
     * @param clientId an UUID string represents client ID
     * @param lockKey the key of a lock
     * @return the json message of SimpleMsg
     */
    @Override
    public String handleClientRelease(String clientId, String lockKey) {

        return forwardMsgToLeader(clientId, lockKey, Message.RELEASE);
    }

    /**
     * process the input message based on its type
     * @param input the input messge as json
     * @return the json message of SimpleMsg
     *
     * @see Message
     * @see ClientMsg
     * @see SimpleMsg
     */
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
                result = Message.ECHO_BROADCAST;
                break;
            case Message.RELEASE:
                result = handleClientRelease(msg.getClientId(),
                        (String)msg.getMessageContent());
                break;
            default:
                result = "";

        }
        return result;
    }
}