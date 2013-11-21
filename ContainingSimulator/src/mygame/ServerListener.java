/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hendrik
 */
public class ServerListener {

    private String serverName;//server host
    private int port;//port number of server
    private BufferedReader input;//input stream from client
    private Socket client;//client's socket connection
    private PrintWriter output;//output stream from client
    private Main main;//maingame to send message recieved

    /**
     * Listener to server and sender to server
     * @param main
     */
    public ServerListener(Main main) {

        serverName = "127.0.0.1";
        port = 6066;
        this.main = main;
        Thread listenerThread = new Thread(new Runnable() {
            public void run() {
                ServerListener.this.run();
            }
        });
        listenerThread.start();
    }

    /**
     * main methoded that is listining to server
     */
    public void run() {
        try {
            System.out.println("Connecting to " + serverName
                    + " on port " + port);
            client = new Socket(serverName, port);
            System.out.println("Just connected to "
                    + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            output =
                    new PrintWriter(outToServer, true);

            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         
        while (true) {
            String s;
            try {
                if (input.ready()) {
                    s = input.readLine();
                    if (!s.isEmpty()) {
                        System.out.println("message from ip:" + client.getRemoteSocketAddress());
                        System.out.println(s);
                        main.messageRecieved(Message.decodeMessage(s));
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * Send message to server
     * @param message the message you want to send
     */
    void sendMessage(Message message) {
        System.out.println("message to ip:" + client.getRemoteSocketAddress());
        output.println(Message.encodeMessage(message));
    }
}
