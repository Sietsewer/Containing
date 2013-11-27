/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.SimContainer;
import containing.xml.CustomVector3f;
import containing.xml.Message;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
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
    List<Crane> lorreyCranes;
    List<Crane> seaCranes;
    List<Crane> bargeCranes;
    List<Crane> trainCranes;
    HashMap<AGV, Crane> waitingToBeReadyAtCrane;
    HashMap<Crane, AGV> waitingForCraneToPickUpFromAgv;
    HashMap<Crane, AGV> waitingForCraneToPutToAgv;
    HashMap<Crane, Transporter> dockedTransporter;

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
        waitingToBeReadyAtCrane = new HashMap<>();
        waitingForCraneToPickUpFromAgv = new HashMap<>();
        waitingForCraneToPutToAgv = new HashMap<>();
        pathFinder = new PathFinder();
        pathFinder.createMap();
        dockedTransporter = new HashMap<>();
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

        bargeCranes = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            Crane c = new Crane("CBA" + String.format("%03d", i));
            c.node = pathFinder.getMapCBA().get(i - 1);
            bargeCranes.add(c);
            PrintMessage("Bargecrane Created - " + c.toString());
        }

        seaCranes = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Crane c = new Crane("CSE" + String.format("%03d", i));
            c.startRange = (i - 1) * 10;
            c.range = 10;
            c.node = pathFinder.getMapCSE().get(i - 1);
            seaCranes.add(c);
            PrintMessage("SeaCrane Created - " + c.toString());
        }

        trainCranes = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Crane c = new Crane("CTR" + String.format("%03d", i));
            c.node = pathFinder.getMapCTR().get(i - 1);
            trainCranes.add(c);
            PrintMessage("Traincrane Created - " + c.toString());
        }


        lorreyCranes = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Crane c = new Crane("CLO" + String.format("%03d", i));
            c.node = pathFinder.getMapCLO().get(i - 1);
            lorreyCranes.add(c);
            PrintMessage("Lorreystop Created - " + c.toString());
        }
        //Create buffers and agv's
        for (int i = 1; i <= 63; i++) {
            Buffer b = new Buffer();
            Crane c = new Crane("CBU" + String.format("%03d", i));
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
        PrintMessage("Total Lorreycranes - " + lorreyCranes.size());
        PrintMessage("Total Seacranes - " + seaCranes.size());
        PrintMessage("Total Bargecranes - " + bargeCranes.size());
        PrintMessage("Total trainCrane - " + trainCranes.size());
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

    void sendMessage(Message Message) {
        server.sendCommand(Message);
        this.PrintMessage("Message send - " + Message.toString());
    }

    /**
     * tick in simulator to give commands
     */
    public void timerTick() {
        PrintMessage("simtime is now: " + timeFormat.format(simTime));

        List<Container> _temp = new ArrayList<>();
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
            List<Transporter> transporters = new ArrayList<>();
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

                    for (Crane crane : lorreyCranes) {
                        if (!dockedTransporter.containsKey(crane)) {
                            trolley.setDockingPoint(crane);
                            dockedTransporter.put(crane, trolley);
                            break;
                        }
                    }
                    transporters.add(trolley);
                    trolley = new Transporter(TransportTypes.TROLLEY);

                }
            }
            if (seaShip.getContainerCount() > 0) {

                transporters.add(seaShip);
                for (int i = 0; i < 10; i += 2) {
                    if (!dockedTransporter.containsKey(seaCranes.get(i))) {

                        Crane c = seaCranes.get(i);

                        seaShip.setDockingPoint(c);

                        dockedTransporter.put(c, seaShip);

                        break;
                    }
                }

            }
            if (barge.getContainerCount() > 0) {
                for (int i = 0; i < 8; i += 2) {
                    if (!dockedTransporter.containsKey(bargeCranes.get(i))) {

                        Crane c = bargeCranes.get(i);

                        barge.setDockingPoint(c);

                        dockedTransporter.put(c, barge);

                        break;
                    }
                }
                transporters.add(barge);
            }
            if (train.getContainerCount() > 0) {
                for (int i = 0; i < 4; i++) {
                    if (!dockedTransporter.containsKey(trainCranes.get(i))) {

                        Crane c = trainCranes.get(i);

                        train.setDockingPoint(c);

                        dockedTransporter.put(c, train);

                        break;
                    }
                }
                transporters.add(train);
            }
            if (transporters.size() > 0) {
                for (Transporter t : transporters) {

                    currentTransporter.add(t);

                    PrintMessage("Arriving: " + t.toString());

                    Message m = new Message(Commands.CREATE_TRANSPORTER, null);

                    Object[] objects = new Object[t.getContainerCount() + 3];
                    objects[0] = t.id;
                    objects[1] = t.getTransportType();
                    objects[2] = t.getDockingPoint().id;

                    for (int i = 0; i < t.getContainerCount(); i++) {
                        objects[i + 3] = new SimContainer(t.getContainer(i));
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
    public final void PrintMessage(String message) {
        window.WriteLogLine(message);
    }

    private void AGVReady(AGV agv) {
        this.PrintMessage("AGV ready - " + agv.name);
        agv.setIsReady(true);
        if (waitingToBeReadyAtCrane.containsKey(agv)) {

            Crane c = waitingToBeReadyAtCrane.get(agv);
            if (c.getReady()) {
                putContainer(agv, c);
            }
        }

    }

    private AGV getValueFromHashmap(HashMap<AGV, Crane> collection, Crane c) {
        Iterator it = collection.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            if (pairs.getValue() == c) {
                it.remove();
                return (AGV) pairs.getKey();

            }
            it.remove();
        }
        return null;
    }

    /**
     * Put container onto agv
     *
     * @param agv
     * @param crane
     */
    public void putContainer(AGV agv, Crane crane) {
        Message m = new Message(Commands.GIVE_CONTAINER, null);
        m.setParameters(new Object[]{agv.name, crane.id});
        this.sendMessage(m);
        agv.container = crane.container;
        crane.container = null;
        agv.ready = false;
        crane.setIsReady(false);
        waitingForCraneToPutToAgv.put(crane, agv);
    }

    private void craneReady(Crane c) {
        this.PrintMessage("Crane ready - " + c.id);
        c.setIsReady(true);
        if (waitingToBeReadyAtCrane.containsValue(c)) {

            AGV agv = getValueFromHashmap(waitingToBeReadyAtCrane, c);
            if (agv.getIsReady()) {

                putContainer(agv, c);

            }
        } else if (waitingForCraneToPickUpFromAgv.containsKey(c)) {
            AGV v = waitingForCraneToPickUpFromAgv.get(c);
            v.moveToHome(c, this);
            waitingForCraneToPickUpFromAgv.remove(c);

        }//Kraan heeft container aan AGV gegeven
        else if (waitingForCraneToPutToAgv.containsKey(c)) {
            //AGV wordt naar huis gestuurd
            AGV v = waitingForCraneToPutToAgv.get(c);
            v.container = c.container;
            c.container = null;
            v.moveToHome(c, this);
            waitingForCraneToPutToAgv.remove(c);

            //Container krijgt opdracht om nieuwe container te pakken
            Transporter t = dockedTransporter.get(c);

            if (t.getContainerCount() > 0) {
                CustomVector3f lastPosistion = c.container.getPosition();
                Container toMove = null;
                for (int x = (int) lastPosistion.x; x < c.range && toMove == null; x++) {
                    for (int z = (int) lastPosistion.z; z <= 15 && toMove == null; z++) {
                        for (int y = 5; y >= 0 && toMove == null; y--) {
                            for (Container cont : t.getContainers()) {
                                if (cont.getPosition().x == x
                                        && cont.getBufferPosition().z == z
                                        && cont.getPosition().y == y) {
                                    toMove = cont;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (toMove == null) {
                    c.setIsReady(true);
                } else {
                    Message m = new Message(Commands.PICKUP_CONTAINER, null);
                    ArrayList<Object> params = new ArrayList<>();
                    params.add(c.id);

                    params.add(toMove.getId());

                    params.add(toMove.getPosition().x);

                    params.add(toMove.getPosition().y);

                    params.add(toMove.getPosition().z);

                    m.setParameters(params.toArray());

                    this.sendMessage(m);
                    c.setIsReady(false);
                    for (Buffer b : buffers) {
                        CustomVector3f bestpos = b.findBestBufferPlace(toMove);
                        AGV agv = b.AGVAvailable();
                        if (bestpos != null && agv != null) {
                            toMove.setBufferPosition(bestpos);
                            b.reservePosition(toMove);
                            agv.moveToCrane(c, this);
                            waitingToBeReadyAtCrane.put(agv, c);
                            agv.isHome = false;
                            agv.ready = false;
                        }
                    }
                }
            } else {
                Message m = new Message(Commands.REMOVE_TRANSPORTER, new Object[]{dockedTransporter.get(c).id});
                this.sendMessage(m);
                currentTransporter.remove(t);
                dockedTransporter.remove(c);
                c.ready = true;
            }
        }

    }

    /**
     * Crane from buffer is ready
     *
     * @param b
     */
    public void bufferCraneReady(Buffer b) {
    }

    private void transporterReady(Transporter t) {
    }

    void setContainers(List<Container> containers) {
        allContainers = containers;
        Collections.sort(containers, new ContainerComparer());
        for (Container c : containers) {
            this.PrintMessage("Loaded - " + c.toString());
        }
        this.PrintMessage("Total containers - " + containers.size());

    }

    /**
     * message has been recieved from simulator
     *
     * @param message the message object that has been recieved
     */
    public void recievedMessage(String message) {
        PrintMessage(message);
        Message m = Message.decodeMessage(message);
        if (m.getParameters()[0] instanceof String) {
            int id = Integer.getInteger(((String) m.getParameters()[0]).substring(3, 5));
            switch (m.getCommand()) {
                case Commands.READY:
                    switch (((String) m.getParameters()[0]).charAt(0)) {
                        case 'C':
                            if (((String) m.getParameters()[0]).substring(1, 2).equalsIgnoreCase("SE")) {
                                craneReady(seaCranes.get(id - 1));
                            } else if (((String) m.getParameters()[0]).substring(1, 2).equalsIgnoreCase("BA")) {
                                craneReady(bargeCranes.get(id - 1));
                            } else if (((String) m.getParameters()[0]).substring(1, 2).equalsIgnoreCase("LO")) {
                                craneReady(lorreyCranes.get(id - 1));
                            } else if (((String) m.getParameters()[0]).substring(1, 2).equalsIgnoreCase("TR")) {
                                craneReady(trainCranes.get(id - 1));
                            } else if (((String) m.getParameters()[0]).substring(1, 2).equalsIgnoreCase("BU")) {
                                bufferCraneReady(buffers.get(id - 1));
                            }
                            break;
                        case 'T':
                            for (Transporter t : currentTransporter) {
                                if (t.id.equalsIgnoreCase(((String) m.getParameters()[0]))) {
                                    transporterReady(t);
                                    break;
                                }
                            }
                            break;
                        case 'A':
                            AGVReady(agvs.get(Integer.getInteger(((String) m.getParameters()[0]).substring(3, 5))));
                            break;
                    }

                    break;
            }
        }
    }

    /**
     * get path finder
     *
     * @return
     */
    public PathFinder getPathFinder() {
        return pathFinder;
    }

    String getAndroidData() {

        Message m = new Message();
        m.setCommand(Commands.READY);

        int containerCount = 0;
        int containerCountInTransporter = 0;
        int containerCountInBuffer = 0;
        int containerCraneCount = 0;
        int containerCountAGV = 0;

        for (Transporter t : currentTransporter) {
            containerCountInTransporter += t.getContainerCount();
        }
        containerCount += containerCountInTransporter;

        for (Buffer b : buffers) {
            containerCountInBuffer += b.getContainerCount();

        }
        containerCount += containerCountInBuffer;

        List<Crane> cranes = new ArrayList<>();
        cranes.addAll(bargeCranes);
        cranes.addAll(lorreyCranes);
        cranes.addAll(seaCranes);
        cranes.addAll(trainCranes);

        for (Crane c : cranes) {
            if (c.container != null) {
                containerCraneCount++;

            }
        }
        containerCount += containerCraneCount;

        for (AGV a : agvs) {
            if (a.container != null) {
                containerCountAGV++;

            }
        }
        containerCount += containerCountAGV;

        m.setParameters(new Object[]{containerCount, containerCountInTransporter, containerCountInBuffer, containerCraneCount, containerCountAGV});
        return Message.encodeMessage(m);

    }
}
