/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hendrik
 */
public class ServerClient {

    private BufferedReader input;
    private Socket client;
    private PrintWriter output;
    private Server server;

    public ServerClient(Socket client, Server s) {
        this.client = client;
        server = s;
    }

    public void sendMessage(String message) {
        output.println(message);
    }

    public void Run() {
        try {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Read failed");
        }
        while (true) {
            try {
                String s = input.readLine();
                if (!s.isEmpty()) {
                    server.MessageRecieved(s);
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
