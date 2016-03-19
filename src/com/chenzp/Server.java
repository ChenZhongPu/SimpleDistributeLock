package com.chenzp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenzhongpu on 3/17/16.
 */
public class Server {

    public static ConcurrentHashMap<String, String> lockMap = new ConcurrentHashMap<String, String>();

    public static Tuple followsAndLeader = null;

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("error..");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean isLeader = Boolean.parseBoolean(args[1]);
        List<ServerBean> servers = SystemUtil.loadServers();
        followsAndLeader = SystemUtil.getFollowsAndLeader(servers);

        try{
           // ServerSocket serverSocket = new ServerSocket(portNumber);
            ServerSocket serverSocket = new ServerSocket(portNumber);

            while (true) {
                System.out.println("start of while...");
                new Thread(new ServerRunable(serverSocket.accept(), isLeader)).start();
//                Socket s = serverSocket.accept();
//                PrintWriter out =
//                        new PrintWriter(s.getOutputStream(), true);
//                BufferedReader in = new BufferedReader(
//                        new InputStreamReader(
//                                s.getInputStream()));

//                if (in.readLine() == null) {
//                    System.out.println("in.readline is null");
//                }
//                else {
//                    System.out.println("in.readline is not null");
//                }
                //out.println("from server .. ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

class ServerRunable implements Runnable {

    private Socket socket = null;
    private boolean isLeader = false;

    public ServerRunable(Socket socket, boolean isLeader) {
        this.socket = socket;
        this.isLeader = isLeader;
    }

    public void run() {
        try {
            System.out.println("in the runing...");
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            ServerProtocol serverProtocol;
            if (isLeader) {
                System.out.println("is leader");
                serverProtocol = new LeaderServerProtocol((List<ServerBean>) Server.followsAndLeader.first);
            } else {
                serverProtocol = new FollowerServerProtocol((ServerBean) Server.followsAndLeader.second);
            }
            String echo = serverProtocol.processInput(in.readLine());
            System.out.println("return back from echo.." + echo);
            if (echo != null) {

                out.println(echo);
            }
            System.out.println("print the map...");
            for (ConcurrentHashMap.Entry<String, String> entry : Server.lockMap.entrySet()) {
                System.out.println(entry.getKey());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}