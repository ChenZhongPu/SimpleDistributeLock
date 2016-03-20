package com.team6.sjtu;

/**
 * Created by chenzhongpu on 3/17/16.
 */
public class ServerBean {

    String address;
    int port;
    boolean isLeader;

    ServerBean(String address, int port, boolean isLeader) {
        this.address = address;
        this.port = port;
        this.isLeader = isLeader;
    }
}