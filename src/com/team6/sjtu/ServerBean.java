package com.team6.sjtu;

/**
 * Created by chenzhongpu on 3/17/16.
 *
 * ServerBean is a java bean, describing the basic server infomation,
 * including address, port, and whether it is a leader
 */
public class ServerBean {

    String address;
    int port;
    boolean isLeader;

    /**
     *
     * @param address Server address
     * @param port  listening port
     * @param isLeader true if it is leader; false if it is follower
     */
    ServerBean(String address, int port, boolean isLeader) {
        this.address = address;
        this.port = port;
        this.isLeader = isLeader;
    }
}