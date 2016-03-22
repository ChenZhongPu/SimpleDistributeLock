package com.team6.sjtu;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenzhongpu on 3/17/16.
 *
 * Server is the class for both leader and follower servers which
 * accepts scokets from clients.
 *
 * Server creates a thread for each request.
 */
public class Server {

    /**
     * the replicated map
     */
    public static ConcurrentHashMap<String, String> lockMap = new ConcurrentHashMap<String, String>();

    /**
     * @see Tuple
     * the tuple store follower server and leader server.
     *
     * The first item is follower, and the second one is leader.
     */
    static Tuple followsAndLeader = null;

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Error: java com.team6.sjtu.Server --port --[true][false] ");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean isLeader = Boolean.parseBoolean(args[1]);
        List<ServerBean> servers = SystemUtil.loadServers();
        followsAndLeader = SystemUtil.getFollowsAndLeader(servers);

        try{

            ServerSocket serverSocket = new ServerSocket(portNumber);

            while (true) {

                new Thread(new ServerRunable(serverSocket.accept(), isLeader)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

/**
 * This class is servered for multi-thread
 */
class ServerRunable implements Runnable {

    private Socket socket = null;
    private boolean isLeader = false;

    /**
     *
     * @param socket the accepted socket from client
     * @param isLeader true if it is in Leader; false otherwise
     */
    public ServerRunable(Socket socket, boolean isLeader) {
        this.socket = socket;
        this.isLeader = isLeader;
    }

    /**
     * @see ServerProtocol
     * @see Message
     * To process the request based on different message prototol
     */
    @Override
    public void run() {
        try {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            ServerProtocol serverProtocol;
            if (isLeader) {
                serverProtocol = new LeaderServerProtocol((List<ServerBean>) Server.followsAndLeader.first);
            } else {
                serverProtocol = new FollowerServerProtocol((ServerBean) Server.followsAndLeader.second);
            }

            String input = in.readLine();

            while (input != null && !input.equals(Message.BYE)) {

                // ignore the HELLO message
                if (! input.equals(Message.HELLO)) {

                    String echo = serverProtocol.processInput(input);

                    if (echo != null && echo.equals(Message.ECHO_BROADCAST)) {
                        break;
                    }

                    if (echo != null) {
                        out.println(echo);
                    }

                }

                input = in.readLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}