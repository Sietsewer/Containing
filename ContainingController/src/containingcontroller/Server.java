/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Hendrik
 */
public class Server {

    private ServerSocket serverSocket;
    Controller controller;
    ArrayList<ServerClient> clients;//list of connected clients

    /**
     * create server
     *
     * @param c controller
     * @throws IOException
     */
    public Server(Controller c) throws IOException {
        serverSocket = new ServerSocket(6066);
        serverSocket.setSoTimeout(100000000);
        controller = c;
        clients = new ArrayList<>();
    }

    /**
     * main run function listining to clients
     */
    public void Run() {
        System.out.println("Server running");
        while (true) {
            try {
                Socket server = serverSocket.accept();
                System.out.println("new client at ip:" + server.getRemoteSocketAddress());
                controller.PrintMessage("Connected to device:" + server.getRemoteSocketAddress());
                final ServerClient c = new ServerClient(server, this);
                clients.add(c);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        c.Run();
                    }
                }).start();

            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * Set simulator to create tranporter
     */
    public void createTransporter() {

        Message message = new Message();
        message.setCommand(Commands.CREATE_TRANSPORTER);
        message.setParameters(null);
        sendCommand(message);

    }

    /**
     * send command to clients
     *
     * @param mes
     * @return
     */
    public boolean sendCommand(Message mes) {
        ServerClient c = null;
        try {
            String xml = Message.encodeMessage(mes);
            xml = xml.replace("\n", "");
            for (int i = 0; i < clients.size(); i++) {
                c = clients.get(i);
                c.sendMessage(xml);
            }
            return true;
        } catch (Exception e) {
            clients.remove(c);
            return false;
        }

    }

    void MessageRecieved(String ln) {
        controller.recievedMessage(ln);
    }
}
