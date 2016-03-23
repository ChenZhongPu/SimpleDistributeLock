package com.team6.sjtu.test;

import com.team6.sjtu.DistributedClient;
import com.team6.sjtu.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by chenzhongpu on 3/22/16.
 */
public class RMain {

    private static List<String> lockKeys;

    private static DistributedClient clientOne;

    private static DistributedClient clientTwo;

    public static Logger logger = LogManager.getLogger(RMain.class);


    public static void main(String[] args) {

        List<String> serverAddressList = new ArrayList<String>();
        serverAddressList.add("192.168.1.10");
        serverAddressList.add("192.168.1.11");
        serverAddressList.add("192.168.1.12");

        Random randomizer = new Random();

        clientOne = new DistributedClient(serverAddressList.get(randomizer.nextInt(serverAddressList.size())));
        clientTwo = new DistributedClient(serverAddressList.get(randomizer.nextInt(serverAddressList.size())));

        new RMain().testRandom();

    }


    public void testRandom() {


        new Thread(new SendMsgRunnable(clientOne)).start();
        new Thread(new SendMsgRunnable(clientTwo)).start();
        System.out.println("See the log file to check");

    }
}

class SendMsgRunnable implements Runnable {

    private DistributedClient client;

    private List<String> lockKeys;

    private Integer[] types;

    private Random randomizer;

    public SendMsgRunnable(DistributedClient client) {

        this.client = client;

        String[] locks = new String[]{"lockkey1", "lockkey2", "lockkey3", "lockkey4", "lockkey5"};
        lockKeys = new ArrayList<String>();
        lockKeys.addAll(Arrays.asList(locks));

        types = new Integer[]{Message.APPLY, Message.RELEASE};

        randomizer = new Random();

    }

    @Override
    public void run() {

        for (int i = 0; i < 10; i++) {
            String lockKey = lockKeys.get(randomizer.nextInt(lockKeys.size()));
            int messageType = types[randomizer.nextInt(2)];
            boolean echo;
            switch (messageType){
                case Message.APPLY:
                    RMain.logger.info("trylock " + lockKey);
                    echo =  client.tryLock(lockKey);
                    RMain.logger.info("trylock " + lockKey + ". Echo = " + echo);
                    break;
                case Message.RELEASE:
                    RMain.logger.info("release " + lockKey);
                    echo =  client.unLock(lockKey);
                    RMain.logger.info("release " + lockKey + ". Echo = " + echo);
                    break;
            }

        }
    }
}
