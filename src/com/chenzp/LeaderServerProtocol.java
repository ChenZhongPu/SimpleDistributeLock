package com.chenzp;

import java.util.List;
import java.util.Map;

/**
 * Created by chenzhongpu on 3/17/16.
 */
public class LeaderServerProtocol extends ServerProtocol {

    private List<ServerBean> follows;

    public LeaderServerProtocol(List<ServerBean> follows) {
        super();
        this.follows = follows;
    }

    private void broadcast() {

        ClientMsg broadcastMsg = new ClientMsg();
        broadcastMsg.setMessageType(Message.BROADCAST);
        broadcastMsg.setMessageContent(gson.toJson(Server.lockMap));
        String msg = gson.toJson(broadcastMsg);

        for (ServerBean followServer : follows) {

            SystemUtil.sendTCPMsg(msg, SystemUtil.LOCALADDRESS, followServer.port, true);
        }
    }

    @Override
    public String handleClientApply(String messageId, String clientId, String lockKey) {
        SimpleMsg msg = new SimpleMsg();
        msg.setMessageId(messageId);
        msg.setMessageType(Message.ECHOAPPLY);
        msg.setMessageContent(false);
        if (Server.lockMap.get(lockKey) == null) {
            // if is null, nobody lock it
            // this client can lock it
            Server.lockMap.put(lockKey, clientId);
            msg.setMessageContent(true);

            ClientMsg broadcastMsg = new ClientMsg();
            broadcastMsg.setMessageType(Message.BROADCAST);
            broadcastMsg.setMessageContent(gson.toJson(Server.lockMap));
            SystemUtil.sendTCPMsg(gson.toJson(broadcastMsg), SystemUtil.LOCALADDRESS, 4445, true);

        }

        return gson.toJson(msg);
    }

    @Override
    public String handleClientRelase(String messageId, String clientId, String lockKey) {
        SimpleMsg msg = new SimpleMsg();
        msg.setMessageType(Message.ECHORELEASE);
        msg.setMessageId(messageId);
        msg.setMessageContent(false);

        String owner = Server.lockMap.get(lockKey);

        if (owner != null && owner.equals(clientId)) {
            Server.lockMap.remove(lockKey);
            msg.setMessageContent(true);

            // broadcast
            ClientMsg broadcastMsg = new ClientMsg();
            broadcastMsg.setMessageType(Message.BROADCAST);
            broadcastMsg.setMessageContent(gson.toJson(Server.lockMap));
            SystemUtil.sendTCPMsg(gson.toJson(broadcastMsg), SystemUtil.LOCALADDRESS, 4445, true);
        }

        return gson.toJson(msg);
    }

    @Override
    public String processInput(String input) {

        ClientMsg msg = gson.fromJson(input, ClientMsg.class);
        String result;

        switch (msg.getMessageType()) {
            case Message.CHECKISOWN:
                System.out.println("run processinput... checkisown");
                result = handleClientCheckOwn(msg.getMessageId(), msg.getClientId(),
                        (String)msg.getMessageContent());
                break;
            case Message.APPLY:
                result = handleClientApply(msg.getMessageId(), msg.getClientId(),
                        (String)msg.getMessageContent());
                break;
            case Message.RELEASE:
                result = "";
                break;
            default:
                result = "";

        }
        return result;
    }

}
