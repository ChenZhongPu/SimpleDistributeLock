package com.chenzp;

import java.util.UUID;

/**
 * Created by chenzhongpu on 3/15/16.
 */
public class DistributedClient {
    private String clientId;
    private boolean isConnected;
    public DistributedClient(String serverAddress) {
        this.isConnected = true;
        this.clientId = getClientId();
    }

    private String getClientId() {
        return UUID.randomUUID().toString();
    }


}
