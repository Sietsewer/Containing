/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hendrik
 */
public class Controller {

    private Server server;//Server for communication between applications
    private ControllerWindow window;//Mainwindow to print log
    List<Container> allContainers;// this list holds all containers
    Map<Date, Container> sortedContainers;//containers sorted by arrivaldate

    /**
     *
     * @param window mainwindow needed to write to textarea
     */
    public Controller(ControllerWindow window) {
        this.window = window;
        try {
            server = new Server(this);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                server.Run();
            }
        }).start();
    }

    /**
     * Print message on window
     *
     * @param message
     */
    public void PrintMessage(String message) {
        window.WriteLogLine(message);
    }

    /**
     * message has been recieved from simulator
     *
     * @param message the message object that has been recieved
     */
    public void recievedMessage(String message) {
        PrintMessage(message);
    }

    void setContainers(List<Container> containers) {
        allContainers = containers;
        Collections.sort(containers, new ContainerComparer());
        for (Container c : containers) {
            this.PrintMessage("Loaded - "+ c.toString());
        }

    }
}
