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
    public List<Transporter> allArivingTransporters;
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
        allArivingTransporters = new ArrayList<>();
        //Set time of simulator
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2004);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 22);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        simTime = cal.getTime();

        bargeCranes = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            Crane c = new Crane("CBA" + String.format("%03d", i), Crane.BargeCrane);
            c.node = pathFinder.getMapCBA().get(i - 1);

            bargeCranes.add(c);
            PrintMessage("Bargecrane Created - " + c.toString());
        }

        seaCranes = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Crane c = new Crane("CSE" + String.format("%03d", i), Crane.SeaCrane);


            c.node = pathFinder.getMapCSE().get(i - 1);
            seaCranes.add(c);
            PrintMessage("SeaCrane Created - " + c.toString());
        }

        trainCranes = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Crane c = new Crane("CTR" + String.format("%03d", i), Crane.TrainCrane);
            c.node = pathFinder.getMapCTR().get(i - 1);

            trainCranes.add(c);
            PrintMessage("Traincrane Created - " + c.toString());
        }


        lorreyCranes = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Crane c = new Crane("CLO" + String.format("%03d", i), Crane.LorryCrane);
            c.node = pathFinder.getMapCLO().get(i - 1);
            lorreyCranes.add(c);
            PrintMessage("Lorreystop Created - " + c.toString());
        }
        //Create buffers and agv's
        for (int i = 1; i <= 63; i++) {
            Buffer b = new Buffer();
            Crane c = new Crane("CBU" + String.format("%03d", i), Crane.BufferCrane);
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
        //    this.PrintMessage("Message send - " + Message.toString());
    }

    /**
     * tick in simulator to give commands
     */
    public void timerTick() {
        List<Transporter> arrivingTransporters = new ArrayList<>();
        for (int i = 0; i < allArivingTransporters.size(); i++) {
            if (allArivingTransporters.get(i).getContainer(0).getDateArrival().before(simTime)) {

                arrivingTransporters.add(allArivingTransporters.get(i));
                allArivingTransporters.remove(i);
                i--;
            } else {
                break;
            }
        }
        for (Transporter t : arrivingTransporters) {

            switch (t.getTransportType()) {
                case TransportTypes.BARGE:

                    for (int i = 0; i < 8; i += 2) {
                        if (!dockedTransporter.containsKey(bargeCranes.get(i))) {

                            Crane c = bargeCranes.get(i);

                            t.setDockingPoint(c);

                            dockedTransporter.put(c, t);

                            break;
                        }
                    }
                    break;
                case TransportTypes.SEASHIP:
                    for (int i = 0; i < 10; i += 2) {
                        if (!dockedTransporter.containsKey(seaCranes.get(i))) {

                            Crane c = seaCranes.get(i);

                            t.setDockingPoint(c);

                            dockedTransporter.put(c, t);

                            break;
                        }
                    }
                    break;
                case TransportTypes.TRAIN:
                    for (int i = 0; i < 4; i++) {
                        if (!dockedTransporter.containsKey(trainCranes.get(i))) {

                            Crane c = trainCranes.get(i);

                            t.setDockingPoint(c);

                            dockedTransporter.put(c, t);

                            break;
                        }
                    }
                    break;
                case TransportTypes.TROLLEY:
                    for (Crane crane : lorreyCranes) {
                        if (!dockedTransporter.containsKey(crane)) {
                            t.setDockingPoint(crane);
                            dockedTransporter.put(crane, t);
                            break;
                        }
                    }
                    break;

            }
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
                waitingToBeReadyAtCrane.remove(agv);


            }
        }

    }

    private void createTransporters() {
        Container previousContainer = null;
        Transporter currentTransporter = null;
        allArivingTransporters.clear();
        for (int i = 0; i < allContainers.size(); i++) {
            Container c = allContainers.get(i);
            if (previousContainer == null) {
                currentTransporter = new Transporter(c.getTransportTypeArrival());
                currentTransporter.loadContainer(c);
            } else {
                if (c.getDateArrival().getTime() == previousContainer.getDateArrival().getTime()
                        && c.getTransportTypeArrival() == currentTransporter.getTransportType()
                        && c.getTransportTypeArrival() != TransportTypes.TROLLEY) {
                    currentTransporter.loadContainer(c);
                } else {
                    allArivingTransporters.add(currentTransporter);
                    currentTransporter = new Transporter(c.getTransportTypeArrival());
                    currentTransporter.loadContainer(c);
                }

            }
            previousContainer = c;
            allContainers.remove(i);
            i--;
        }
        allArivingTransporters.add(currentTransporter);
    }

    private AGV getValueFromHashmap(HashMap<AGV, Crane> collection, Crane c) {
        for (Map.Entry<AGV, Crane> e : collection.entrySet()) {
            AGV key = e.getKey();
            Crane value = e.getValue();
            if (value.id.equalsIgnoreCase(c.id)) {
                return key;
            }
        }
        return null;
    }

    private Crane getDockingPoint(Transporter t) {
        for (Map.Entry<Crane, Transporter> e : dockedTransporter.entrySet()) {
            Crane key = e.getKey();
            Transporter value = e.getValue();
            if (t.id.equalsIgnoreCase(value.id)) {
                return key;
            }

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
        m.setParameters(new Object[]{crane.id, agv.name});
        this.sendMessage(m);
        agv.container = crane.container;
        crane.container = null;
        agv.setReady(false);
        crane.setIsReady(false);
        waitingForCraneToPutToAgv.put(crane, agv);
    }

    private void craneReady(Crane c) {
        this.PrintMessage("Crane ready - " + c.id);
        c.setIsReady(true);
        if (waitingToBeReadyAtCrane.containsValue(c)) {
            System.out.println("waiting agv ");
            AGV agv = getValueFromHashmap(waitingToBeReadyAtCrane, c);
            if (agv.getIsReady()) {

                putContainer(agv, c);
                waitingToBeReadyAtCrane.remove(agv);

            }
        } else if (waitingForCraneToPickUpFromAgv.containsKey(c)) {
            AGV v = waitingForCraneToPickUpFromAgv.get(c);
            v.moveToHome(c, this);
            waitingForCraneToPickUpFromAgv.remove(c);

        }//Kraan heeft container aan AGV gegeven
        else if (waitingForCraneToPutToAgv.containsKey(c)) {
            System.out.println("Move agv home");

            //AGV wordt naar huis gestuurd
            AGV v = waitingForCraneToPutToAgv.get(c);


            v.moveToHome(c, this);
            waitingForCraneToPutToAgv.remove(c);

            //Container krijgt opdracht om nieuwe container te pakken
            Transporter t = dockedTransporter.get(c);

            if (t.getContainerCount() > 0) {
                CustomVector3f lastPosistion = v.container.getPosition();
                Container toMove = null;
                for (int x = c.startRange; x <= c.range + c.startRange && toMove == null; x++) {
                    for (int z = 0; z <= 16 && toMove == null; z++) {
                        for (int y = 6; y >= 0 && toMove == null; y--) {
                            for (Container cont : t.getContainers()) {
                                if (cont.getPosition().x == x
                                        && cont.getPosition().z == z
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
                    if (dockedTransporter.containsKey(c)) {
                        dockedTransporter.remove(c);
                    }
                    System.out.println("no containers any more");
                } else {
                    Message m = new Message(Commands.PICKUP_CONTAINER, null);
                    ArrayList<Object> params = new ArrayList<>();
                    params.add(c.id);

                    params.add(t.id);

                    params.add(toMove.getId());

                    params.add(toMove.getPosition().x);

                    params.add(toMove.getPosition().y);

                    params.add(toMove.getPosition().z);

                    m.setParameters(params.toArray());
                    t.getContainers().remove(toMove);
                    c.container = toMove;
                    this.sendMessage(m);
                    c.setIsReady(false);
                    sendAGVTo(c, toMove);
                }
            } else {
                if (currentTransporter.contains(t)) {
                    Message m = new Message(Commands.REMOVE_TRANSPORTER, new Object[]{dockedTransporter.get(c).id});
                    this.sendMessage(m);
                    currentTransporter.remove(t);
                }
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
        Crane dockingpoint = getDockingPoint(t);
        List<Crane> _cranes = new ArrayList<Crane>();
        _cranes.add(dockingpoint);
        if (t.getTransportType() == TransportTypes.SEASHIP) {
            int range = t.getLenghtTransporter() / 10;
            if (range < 2) {
                range = 2;
            }
            for (int i = 0; i < seaCranes.size(); i++) {
                if (i * range <= t.getLenghtTransporter() + 1) {
                    seaCranes.get(i).startRange = i * range;
                    seaCranes.get(i).range = range;
                    if (seaCranes.get(i) != dockingpoint) {
                        _cranes.add(seaCranes.get(i));
                    }
                } else {
                    break;
                }
            }
        } else if (t.getTransportType()
                == TransportTypes.BARGE) {
            int range = t.getLenghtTransporter() / 8;
            if (range < 2) {
                range = 2;
            }
            for (int i = 0; i < bargeCranes.size(); i++) {
                if (i * range <= t.getLenghtTransporter()) {
                    bargeCranes.get(i).startRange = i * range;
                    bargeCranes.get(i).range = range;
                    if (bargeCranes.get(i) != dockingpoint) {
                        _cranes.add(bargeCranes.get(i));
                    }
                } else {
                    break;
                }
            }
        }
        else if (t.getTransportType()
                == TransportTypes.TRAIN) {
            int range = t.getLenghtTransporter() / 4;
            if (range < 2) {
                range = 2;
            }
            for (int i = 0; i < trainCranes.size(); i++) {
                if (i * range <= t.getLenghtTransporter()) {
                    trainCranes.get(i).startRange = i * range;
                    trainCranes.get(i).range = range;
                    if (trainCranes.get(i) != dockingpoint) {
                        _cranes.add(trainCranes.get(i));
                    }
                } else {
                    break;
                }
            }
        }
        for (Crane crane : _cranes) {
            Container toMove = null;
            for (int x = crane.startRange; x >= crane.startRange && toMove == null; x--) {
                for (int z = 0; z <= 15 && toMove == null; z++) {
                    for (int y = 5; y >= 0 && toMove == null; y--) {
                        for (Container cont : t.getContainers()) {
                            if (cont.getPosition().x == x
                                    && cont.getPosition().z == z
                                    && cont.getPosition().y == y) {
                                toMove = cont;
                                break;
                            }
                        }
                    }
                }
            }
            if (toMove != null) {
                if (!dockedTransporter.containsKey(crane)) {
                    dockedTransporter.put(crane, t);
                }
                Message m = new Message(Commands.PICKUP_CONTAINER, null);
                ArrayList<Object> params = new ArrayList<>();
                params.add(crane.id);

                params.add(t.id);

                params.add(toMove.getId());

                params.add(toMove.getPosition().x);

                params.add(toMove.getPosition().y);

                params.add(toMove.getPosition().z);

                m.setParameters(params.toArray());

                this.sendMessage(m);
                t.getContainers().remove(toMove);
                crane.container = toMove;
                sendAGVTo(crane, toMove);
            }
        }
    }

    private void sendAGVTo(Crane dockingpoint, Container toMove) {
        dockingpoint.setIsReady(false);
        List<Buffer> preverdBuffers = new ArrayList<>();
        PreferedAGV prefrence = PreferedAGV.BOTH;
        int startBuffer = 1;
        int endBuffer = 63;
        boolean up = true;
        if (dockingpoint.type == Crane.LorryCrane) {
            startBuffer = 34;
            endBuffer = 44;
            prefrence = PreferedAGV.DOWN;
        } else if (dockingpoint.type == Crane.BargeCrane) {
            startBuffer = 14;
            endBuffer = 24;
            prefrence = PreferedAGV.DOWN;
        } else if (dockingpoint.type == Crane.TrainCrane) {
            startBuffer = 6;
            endBuffer = 13;
            prefrence = PreferedAGV.UP;
        }
        for (int i = startBuffer - 1; i < endBuffer - 1; i++) {
            preverdBuffers.add(buffers.get(i));
        }
        if (!sendAGVToCrane(preverdBuffers, dockingpoint, toMove, prefrence)) {
            if (!sendAGVToCrane(buffers, dockingpoint, toMove, prefrence)) {
                sendAGVToCrane(buffers, dockingpoint, toMove, prefrence);
            }
        }
    }

    private boolean sendAGVToCrane(List<Buffer> selectedBuffer, Crane dockingpoint, Container toMove, PreferedAGV up) {
        for (Buffer b : selectedBuffer) {
            CustomVector3f bestpos = b.findBestBufferPlace(toMove);
            AGV agv = null;
            if (up == PreferedAGV.UP) {
                agv = b.AGVAvailable(true);
            } else if (up == PreferedAGV.DOWN) {
                agv = b.AGVAvailable(false);
            } else {
                agv = b.AGVAvailable(false);
                if (agv == null) {
                    agv = b.AGVAvailable(true);
                }
            }

            if (bestpos != null && agv != null) {
                toMove.setBufferPosition(bestpos);
                b.reservePosition(toMove);
                agv.moveToCrane(dockingpoint, this);
                waitingToBeReadyAtCrane.put(agv, dockingpoint);
                agv.setIsHome(false);
                agv.setReady(false);
                return true;
            }
        }
        return false;
    }

    void setContainers(List<Container> containers) {
        allContainers = containers;
        Collections.sort(containers, new ContainerComparer());
        this.PrintMessage("Total containers - " + containers.size());
        createTransporters();
        this.PrintMessage("Total tranporters  - " + allArivingTransporters.size());



    }

    /**
     * message has been recieved from simulator
     *
     * @param message the message object that has been recieved
     */
    public void recievedMessage(String message) {
        //   PrintMessage(message);
        Message m = Message.decodeMessage(message);
        if (!((String) m.getParameters()[0]).equalsIgnoreCase("simulator")) {
            int id = Integer.parseInt(((String) m.getParameters()[0]).substring(3, 6));
            switch (m.getCommand()) {
                case Commands.READY:
                    switch (((String) m.getParameters()[0]).toLowerCase().charAt(0)) {
                        case 'c':
                            if (((String) m.getParameters()[0]).substring(1, 3).equalsIgnoreCase("SE")) {
                                craneReady(seaCranes.get(id - 1));
                            } else if (((String) m.getParameters()[0]).substring(1, 3).equalsIgnoreCase("BA")) {
                                craneReady(bargeCranes.get(id - 1));
                            } else if (((String) m.getParameters()[0]).substring(1, 3).equalsIgnoreCase("LO")) {
                                craneReady(lorreyCranes.get(id - 1));
                            } else if (((String) m.getParameters()[0]).substring(1, 3).equalsIgnoreCase("TR")) {
                                craneReady(trainCranes.get(id - 1));
                            } else if (((String) m.getParameters()[0]).substring(1, 3).equalsIgnoreCase("BU")) {
                                bufferCraneReady(buffers.get(id - 1));
                            }
                            break;
                        case 't':
                            for (Transporter t : currentTransporter) {
                                if (t.id.equalsIgnoreCase(((String) m.getParameters()[0]))) {
                                    transporterReady(t);
                                    break;
                                }
                            }
                            break;
                        case 'a':
                            AGVReady(agvs.get(Integer.parseInt(((String) m.getParameters()[0]).substring(3, 6)) - 1));
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
