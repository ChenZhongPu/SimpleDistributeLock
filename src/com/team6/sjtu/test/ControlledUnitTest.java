package com.team6.sjtu.test;

import com.team6.sjtu.DistributedClient;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.*;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by chenzhongpu on 3/20/16.
 *
 * This is the contolled unit test
 */
public class ControlledUnitTest{

    private static List<String> lockKeys;

    private static DistributedClient clientOne;

    private static DistributedClient clientTwo;


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        String[] locks = new String[]{"lockkey1", "lockkey2", "lockkey3", "lockkey4", "lockkey5"};

        lockKeys = new ArrayList<String>();
        lockKeys.addAll(Arrays.asList(locks));

        List<String> serverAddressList = new ArrayList<String>();
        serverAddressList.add("192.168.1.10");
        serverAddressList.add("192.168.1.11");
        serverAddressList.add("192.168.1.12");

        Random randomizer = new Random();

        clientOne = new DistributedClient(serverAddressList.get(randomizer.nextInt(serverAddressList.size())));
        clientTwo = new DistributedClient(serverAddressList.get(randomizer.nextInt(serverAddressList.size())));
//        clientOne = new DistributedClient("192.168.1.10");
//        clientTwo = new DistributedClient("192.168.1.11");
    }

    @Test
    public void testCheckIsOwn() {

        List<Boolean> results = new ArrayList<Boolean>();

        Random randomizer = new Random();
        int randomIndex = randomizer.nextInt(lockKeys.size());
        String lockKeyOne = lockKeys.get(randomIndex);
        String lockKeyTwo = lockKeys.get((randomIndex + 1) % lockKeys.size());

        results.add(clientOne.checkIsOwn(lockKeyOne)); // false
        results.add(clientOne.tryLock(lockKeyOne));  // true
        results.add(clientTwo.tryLock(lockKeyOne)); // false
        results.add(clientTwo.tryLock(lockKeyTwo)); // true
        results.add(clientOne.unLock(lockKeyOne)); // true
        results.add(clientOne.checkIsOwn(lockKeyOne)); // false
        results.add(clientTwo.tryLock(lockKeyOne)); // true

        assertArrayEquals(results.toArray(),
                new Boolean[]{false, true, false, true, true, false, true});



    }
}

//class SendMsgRunnable implements Runnable {
//
//    private DistributedClient client;
//
//    private int messageType;
//
//    private String lockKey;
//
//    private List<Integer> msgQuene;
//
//    public SendMsgRunnable(DistributedClient client, int messageType, String lockKey) {
//
//        this.client = client;
//        this.messageType = messageType;
//        this.lockKey = lockKey;
//    }
//
//    @Override
//    public void run() {
//        boolean echo;
//        switch (messageType) {
//            case Message.CHECKISOWN:
//                echo = client.checkIsOwn(lockKey);
//                break;
//            case Message.APPLY:
//                echo = client.tryLock(lockKey);
//                break;
//            case Message.RELEASE:
//                echo = client.unLock(lockKey);
//                break;
//            default:
//                echo = false;
//        }
//        System.out.println("in runnable echo = " + echo);
////        synchronized (ConsensusUnitTest.resultMap) {
////            ConsensusUnitTest.resultMap.put(lockKey, echo);
////        }
//    }
//}
