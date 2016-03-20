package com.team6.sjtu.test;

import com.team6.sjtu.DistributedClient;

import com.team6.sjtu.Message;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.*;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by chenzhongpu on 3/20/16.
 */
public class ConsensusUnitTest{

    private static List<String> lockKeys;

    private static DistributedClient clientOne;

    private static DistributedClient clientTwo;

    public static Map<String, Boolean> resultMap;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        String[] locks = new String[]{"lockkey1", "lockkey2", "lockkey3", "lockkey4", "lockkey5"};

        resultMap = Collections.synchronizedMap(new HashMap<String, Boolean>());

        lockKeys = new ArrayList<String>();

        for (String lock : locks) {
            lockKeys.add(lock);
            resultMap.put(lock, false);
        }

        List<String> serverAddressList = new ArrayList<String>();
        serverAddressList.add("192.168.1.10");
        serverAddressList.add("192.168.1.11");
        serverAddressList.add("192.168.1.12");

        Random randomizer = new Random();

        //clientOne = new DistributedClient(serverAddressList.get(randomizer.nextInt(serverAddressList.size())));
        clientOne = new DistributedClient("192.168.1.10");
        clientTwo = new DistributedClient("192.168.1.11");
    }

    @Test
    public void testCheckIsOwn() {

//        new Thread(new SendMsgRunnable(clientOne, Message.CHECKISOWN, lockKeys.get(1))).start();
//        new Thread(new SendMsgRunnable(clientTwo, Message.CHECKISOWN, lockKeys.get(1))).start();

//        new Thread(new SendMsgRunnable(clientOne, Message.APPLY, lockKeys.get(1))).start();
//        new Thread(new SendMsgRunnable(clientTwo, Message.APPLY, lockKeys.get(1))).start();

//        System.out.println("echo " + clientOne.checkIsOwn(lockKeys.get(1)));
//        System.out.println("echo " + clientOne.tryLock(lockKeys.get(1)));

        System.out.println("echo 1 " + clientOne.unLock(lockKeys.get(1)));
        System.out.println("echo 2 " + clientOne.checkIsOwn(lockKeys.get(1)));
        System.out.println("echo 3 " + clientOne.tryLock(lockKeys.get(1)));
        System.out.println("echo 4 " + clientOne.checkIsOwn(lockKeys.get(1)));
        System.out.println("echo 5 " + clientTwo.checkIsOwn(lockKeys.get(1)));
        System.out.println("echo 6 " + clientTwo.tryLock(lockKeys.get(1)));
        System.out.println("echo 7 " + clientOne.unLock(lockKeys.get(1)));
        System.out.println("echo 8 " + clientOne.checkIsOwn(lockKeys.get(1)));
        System.out.println("echo 9 " + clientTwo.tryLock(lockKeys.get(1)));

        String str= "Junit is working fine";
        assertEquals("Junit is working fine",str);



    }
}

class SendMsgRunnable implements Runnable {

    private DistributedClient client;

    private int messageType;

    private String lockKey;

    public SendMsgRunnable(DistributedClient client, int messageType, String lockKey) {

        this.client = client;
        this.messageType = messageType;
        this.lockKey = lockKey;
    }

    @Override
    public void run() {
        boolean echo;
        switch (messageType) {
            case Message.CHECKISOWN:
                echo = client.checkIsOwn(lockKey);
                break;
            case Message.APPLY:
                echo = client.tryLock(lockKey);
                break;
            case Message.RELEASE:
                echo = client.unLock(lockKey);
                break;
            default:
                echo = false;
        }
        System.out.println("in runnable echo = " + echo);
//        synchronized (ConsensusUnitTest.resultMap) {
//            ConsensusUnitTest.resultMap.put(lockKey, echo);
//        }
    }
}
