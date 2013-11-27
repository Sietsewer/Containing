/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hendrik
 */
public class ServerClient {

    private BufferedReader input;//input stream from client
    private Socket client;//client's socket connection
    private PrintWriter output;//output stream from client
    private Server server;//server to relay message's
    private boolean sendOnly = false;

    /**
     *
     * @param client
     * @param s
     */
    public ServerClient(Socket client, Server s) {
        this.client = client;
        server = s;
    }

    ServerClient(Socket client, Server s, boolean b) {
        this(client, s);
        sendOnly = b;
    }

    /**
     * Send message to client, must be in xml format
     *
     * @param message
     */
    public void sendMessage(String message) {
        System.out.println("message to ip:" + client.getRemoteSocketAddress());
        System.out.println(message);
        output.println(message);
    }

    /**
     * main function that listens to client
     */
    public void Run() {
        boolean firstMessage = false;
        try {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Read failed");
        }
        while (true) {
            try {
                if (input.ready()) {
                    String s = input.readLine();
                    if (firstMessage) {
                        Message m = Message.decodeMessage(s);
                                
                         server.controller.PrintMessage("An");
                    } else {
                        if (!s.isEmpty()) {
                            server.MessageRecieved(s);
                            System.out.println("message from ip:" + client.getRemoteSocketAddress());
                            System.out.println(s);
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }

        }
    }
}
