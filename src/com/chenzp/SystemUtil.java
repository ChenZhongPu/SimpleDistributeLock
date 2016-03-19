package com.chenzp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by chenzhongpu on 3/17/16.
 */

public class SystemUtil {

    public static final String LOCALADDRESS = "127.0.0.1";

    public static List<ServerBean> loadServers() {

        List<ServerBean> servers = new ArrayList<ServerBean>();

        try {
            File configFile = new File("serverConfig.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(configFile);

            doc.getDocumentElement().normalize();

            NodeList nodes = doc.getElementsByTagName("server");

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element)node;
                    String address = element.getElementsByTagName("address").item(0).getTextContent();
                    int port = Integer.valueOf(element.getElementsByTagName("port").item(0).getTextContent());
                    boolean isLeader = Boolean.valueOf(element.getElementsByTagName("isLeader").item(0).getTextContent());
                    servers.add(new ServerBean(address, port, isLeader));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return servers;
    }

    public static Tuple<List<ServerBean>, ServerBean> getFollowsAndLeader(List<ServerBean> servers) {

        List<ServerBean> follows = new ArrayList<ServerBean>();
        ServerBean leader = null;
        for (int i = 0; i < servers.size(); i++) {
            ServerBean bean = servers.get(i);
            if (!bean.isLeader) {
                follows.add(servers.get(i));
            } else {
                leader = bean;
            }
        }
        return new Tuple(follows, leader);
    }

    public static String sendTCPMsg(String msg, String address, int port, boolean isBroadCast) {
        try {
            Socket socket = new Socket(address,
                    port);
            PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true);

            out.println(msg);

            if (!isBroadCast) {
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                return in.readLine();
            }
            // if is broadcast, ignore echo massage
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


class Tuple<X, Y> {
    public final X first;
    public final Y second;
    public Tuple(X x, Y y) {
        this.first = x;
        this.second = y;
    }
}

class SimpleMsg {

    protected String messageId;
    protected int messageType;
    protected Object messageContent;

    public SimpleMsg() {

    }

    public SimpleMsg(int messageType, Object messageContent) {

        this.messageType = messageType;
        this.messageContent = messageContent;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Object getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(Object messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public String toString() {
        return "SimpleMsg [messageId= " + messageId + ", messageType= " + messageType +
                ", messageContent= " + messageContent + "]";
    }
}

class ClientMsg extends SimpleMsg {

    private String clientId;

    public ClientMsg() {

    }

    public ClientMsg(int messageType, Object messageContent, String clientId) {

        super(messageType, messageContent);

        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "ClientMsg [messageId= " + messageId + ", messageType= " + messageType + ", messageContent= " +
                messageContent + ", clientId= " + clientId + "]";

    }
}

class Message {
    public static final int CHECKISOWN = 0;
    public static final int ECHOCHECKISOWN = 1;

    public static final int APPLY = 2;
    public static final int ECHOAPPLY = 3;

    public static final int RELEASE = 4;
    public static final int ECHORELEASE = 5;

    public static final int BROADCAST = 6;
    //public static final int ECHOBROADCAST = 7;
}