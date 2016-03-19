package com.chenzp;

import com.google.gson.Gson;

import java.net.Socket;
import java.io.*;
import java.util.UUID;

/**
 * Created by chenzhongpu on 3/17/16.
 */
public class CLient {

    public static void main(String[] args) {
        try{
            Socket echoSocket = new Socket("127.0.0.1", 4445);
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(echoSocket.getInputStream()));
            PrintWriter out =
                    new PrintWriter(echoSocket.getOutputStream(), true);
            //out.println("client say hi...");

          ClientMsg msg = new ClientMsg(Message.CHECKISOWN, "lockkey01", "01");

           // ClientMsg msg = new ClientMsg(Message.APPLY, "lockkey01", "01");
            String msgId = UUID.randomUUID().toString();
            msg.setMessageId(msgId);
            System.out.println("client :" + msgId);
            Gson gson = new Gson();
            out.println(gson.toJson(msg));

            System.out.println(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
