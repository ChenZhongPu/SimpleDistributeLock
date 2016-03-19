package com.chenzp;

import com.google.gson.Gson;

/**
 * Created by chenzhongpu on 3/17/16.
 */

public abstract class ServerProtocol {

    protected Gson gson;

    public ServerProtocol() {
        gson = new Gson();
    }

    public String handleClientCheckOwn(String messageId, String clientId, String lockKey) {
        System.out.println("run handleClientCheckOwn");
        SimpleMsg msg = new SimpleMsg();
        msg.setMessageId(messageId);
        msg.setMessageType(Message.ECHOCHECKISOWN);
        msg.setMessageContent(false);

        String v = Server.lockMap.get(lockKey);
        if (v != null && v.equals(clientId)) {
            System.out.println("==");
            msg.setMessageContent(true);
        }
        return gson.toJson(msg);
    }

    public abstract String handleClientApply(String messageId, String clientId, String lockKey);

    public abstract String handleClientRelase(String messageId, String clientId, String lockKey);

    public abstract String processInput(String Input);
}