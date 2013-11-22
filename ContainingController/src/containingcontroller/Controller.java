/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hendrik
 */
public class Controller {

    PathFinder pathFinder;
    private Server server;//Server for communication between applications
    private ControllerWindow window;//Mainwindow to print log
    List<Container> allContainers;// this list holds all containers
    Timer simTimer;//timer that progresses time in controller
    Date simTime;
    SimpleDateFormat timeFormat;
    List<Transporter> currentTransporter;
    List<Buffer> buffers;
    List<AGV> agvs;
    HashMap<AGV, Crane> waitingToBeReady;

    /**
     *
     * @param window mainwindow needed to write to textarea
     */
    public Controller(ControllerWindow window) {

        this.window = window;

        buffers = new ArrayList<>();
        agvs = new ArrayList<>();
        timeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        currentTransporter = new ArrayList<>();
        waitingToBeReady = new HashMap<>();
        pathFinder = new PathFinder();
        pathFinder.createMap();

        //Set time of simulator
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2004);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 4);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        simTime = cal.getTime();
        
        //Create buffers and agv's
        for (int i = 1; i <= 63; i++) {
            Buffer b = new Buffer();
            Crane c = new Crane("CBF" + String.format("%03d", i));
            b.crane = c;
            PathNode upperNode = pathFinder.getMapBA().get(i - 1);
            PathNode downNode = pathFinder.getMapBB().get(i - 1);
            b.pathNodeUp = upperNode;
            b.pathNodeDown = downNode;
            for (int a = 0; a < 4; a++) {
                if (a < 2) {
                    AGV agv = new AGV(upperNode, b);
                    b.ownedAGV.add(agv);
                    agvs.add(agv);
                    PrintMessage("AGV Created - " + agv.toString());
                } else {
                    AGV agv = new AGV(downNode, b);
                    b.ownedAGV.add(agv);
                    agvs.add(agv);
                    PrintMessage("AGV Created - " + agv.toString());
                }
            }
            PrintMessage("Buffer Created - " + b.toString());
            buffers.add(b);






        }
        PrintMessage("Total AGV - " + agvs.size());
        PrintMessage("Total Buffers - " + buffers.size());
    }

    /**
     * Start simulator with tick of 1 second is 1 second
     */
    public void Start() {
        this.simTimer = new Timer();
        this.simTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerTick();
            }
        }, 0, 1000);
    }

    /**
     * tick in simulator to give commands
     */
    public void timerTick() {
        PrintMessage("simtime is now: " + timeFormat.format(simTime));

        List<Container> _temp = new ArrayList<Container>();
        for (int i = 0; i < allContainers.size(); i++) {

            if (allContainers.get(i).getDateArrival().before(simTime)) {
                _temp.add(allContainers.get(i));
                allContainers.remove(i);
                i--;
            } else {
                break;
            }
        }

        if (_temp.size() > 0) {
            List<Transporter> transporters = new ArrayList<Transporter>();
            Transporter seaShip = new Transporter(TransportTypes.SEASHIP);
            Transporter barge = new Transporter(TransportTypes.BARGE);
            Transporter train = new Transporter(TransportTypes.TRAIN);
            Transporter trolley = new Transporter(TransportTypes.TROLLEY);
            for (Container c : _temp) {
                if (c.getTransportTypeArrival() == TransportTypes.SEASHIP) {
                    seaShip.loadContainer(c);
                } else if (c.getTransportTypeArrival() == TransportTypes.BARGE) {
                    barge.loadContainer(c);
                } else if (c.getTransportTypeArrival() == TransportTypes.TRAIN) {
                    train.loadContainer(c);
                } else if (c.getTransportTypeArrival() == TransportTypes.TROLLEY) {
                    trolley.loadContainer(c);
                    transporters.add(trolley);
                    trolley = new Transporter(TransportTypes.TROLLEY);
                }
            }
            if (seaShip.getContainerCount() > 0) {
                transporters.add(seaShip);
            }
            if (barge.getContainerCount() > 0) {
                transporters.add(barge);
            }
            if (train.getContainerCount() > 0) {
                transporters.add(train);
            }
            if (transporters.size() > 0) {
                for (Transporter t : transporters) {
                    currentTransporter.add(t);
                    PrintMessage("Arriving: " + t.toString());
                    Message m = new Message(Commands.CREATE_TRANSPORTER, null);
                    Object[] objects = new Object[t.getContainerCount() + 2];
                    objects[0] = t.getTransportType();
                    objects[1] = t.id;
                    for (int i = 0; i < t.getContainerCount(); i++) {
                        objects[i + 2] = new SimContainer(t.getContainer(i));
                    }
                    m.setParameters(objects);
                    server.sendCommand(m);
                }
            }
        }





        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(simTime); // sets calendar time/date
        cal.add(Calendar.SECOND, 1); // adds one hour
        simTime = cal.getTime(); // returns new date object, one hour in the future
    }

    /**
     * stop simulation
     */
    public void pause() {
        this.simTimer.cancel();
    }

    /**
     * start server for communication
     */
    public void startServer() {
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
     * create tranporter
     *
     * @param containers
     * @param transportType
     */
    public void createTransporter(List<Container> containers, int transportType) {
    }

    /**
     * message has been recieved from simulator
     *
     * @param message the message object that has been recieved
     */
    public void recievedMessage(String message) {
        PrintMessage(message);
        Message m = Message.decodeMessage(message);
        switch (m.getCommand()) {
            case Commands.READY:
                switch (((String) m.getParameters()[0]).substring(0, 1)) {
                    case "TRS":

                        break;
                    case "SEC":

                        break;
                    case "LOC":

                        break;
                    case "TRC":

                        break;
                    case "BAC":

                        break;
                }

                break;
        }
    }

    void setContainers(List<Container> containers) {
        allContainers = containers;
        Collections.sort(containers, new ContainerComparer());
        for (Container c : containers) {
            this.PrintMessage("Loaded - " + c.toString());
        }
        this.PrintMessage("Total containers - " + containers.size());

    }
}
