package com.team6.sjtu;

import com.google.gson.Gson;

/**
 * Created by chenzhongpu on 3/17/16.
 */

public abstract class ServerProtocol {

    protected Gson gson;

    public ServerProtocol() {
        gson = new Gson();
    }

    public String handleClientCheckOwn(String clientId, String lockKey) {
        SimpleMsg msg = new SimpleMsg();
        msg.setMessageType(Message.ECHOCHECKISOWN);
        msg.setMessageContent(false);

        String v = Server.lockMap.get(lockKey);
        if (v != null && v.equals(clientId)) {
            msg.setMessageContent(true);
        }
        return gson.toJson(msg);
    }

    public abstract String handleClientApply(String clientId, String lockKey);

    public abstract String handleClientRelase(String clientId, String lockKey);

    public abstract String processInput(String Input);
}