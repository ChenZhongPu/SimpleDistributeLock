package com.team6.sjtu;

import com.google.gson.Gson;

import java.net.Socket;
import java.io.*;

/**
 * Created by chenzhongpu on 3/17/16.
 */
public class CLient {

    public static void main(String[] args) {
        try{
            Socket echoSocket = new Socket("127.0.0.1", 4444);
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(echoSocket.getInputStream()));
            PrintWriter out =
                    new PrintWriter(echoSocket.getOutputStream(), true);
            //out.println("client say hi...");

          //ClientMsg msg = new ClientMsg(Message.CHECKISOWN, "lockkey01", "02");

           ClientMsg msg = new ClientMsg(Message.APPLY, "lockkey02", "02");

            Gson gson = new Gson();
            out.println(gson.toJson(msg));

            System.out.println(in.readLine());
            echoSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
