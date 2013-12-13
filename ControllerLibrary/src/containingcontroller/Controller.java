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
import java.util.Collection;
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

    public static int Speed = 1;
    //pathfinde variable
    PathFinder pathFinder;
    //server variables
    private Server server;//Server for communication between applications
    private IWindow window;//Mainwindow to print log
    //timer variables
    Timer simTimer;//timer that progresses time in controller
    Date simTime; // time in the simulation
    SimpleDateFormat timeFormat; // time format for window
    //objects lists
    List<Buffer> buffers;//list of all buffers
    List<AGV> agvs;//list of all agvs
    List<Crane> lorreyCranes;//list of all lorreyCranes
    List<Crane> seaCranes;//list of all seaCranes
    List<Crane> bargeCranes;//list of all bargeCranes
    List<Crane> trainCranes;//list of all trainCranes
    List<Container> containers; //list of all containers
    //run time lists
    List<Transporter> currentTransporter;//list of current transporters on the map
    List<AGV> agvLoadedMovingHome;//
    HashMap<AGV, Crane> waitingToBeReadyAtCrane;
    HashMap<Crane, AGV> waitingForCraneToPickUpFromAgv;
    HashMap<Crane, AGV> waitingForCraneToPutToAgv;
    HashMap<Crane, Transporter> dockedTransporter;
    HashMap<AGV, Crane> waitingForBufferCrane;
    HashMap<Crane, AGV> waitingForBuferCranePickup;
    HashMap<Crane, AGV> waitingForBufferCraneGive;

    List<Crane> puttingToTransporter;
    HashMap<Crane, AGV> pickingupToLoad;
    HashMap<AGV, Crane> movingToLoad;
    HashMap<Container, Crane> containerToCrane;
    List<String> messageLog;
    //list from xml load
    /**
     * list of all transporters that are arriving
     */
    public List<Transporter> allArivingTransporters; // this list holds all loaded transporters
    public List<Transporter> allDepartingTransporters; //this list holds all departing transporters
    ArrayList<Container> allDepartingContainers;
    public boolean playing = false;

    /**
     *
     * @param window mainwindow needed to write to textarea
     */
    public Controller(IWindow window) {

        this.window = window;
        waitingForBuferCranePickup = new HashMap<Crane, AGV>();
        buffers = new ArrayList<Buffer>();
        agvs = new ArrayList<AGV>();
        timeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        currentTransporter = new ArrayList<Transporter>();
        waitingToBeReadyAtCrane = new HashMap<AGV, Crane>();
        waitingForCraneToPickUpFromAgv = new HashMap<Crane, AGV>();
        waitingForCraneToPutToAgv = new HashMap<Crane, AGV>();
        waitingForBufferCraneGive = new HashMap<Crane, AGV>();
        pathFinder = new PathFinder();
        pathFinder.createMap();
        dockedTransporter = new HashMap<Crane, Transporter>();
        allArivingTransporters = new ArrayList<Transporter>();
        allDepartingTransporters = new ArrayList<Transporter>();
        allDepartingContainers = new ArrayList<Container>();
        movingToLoad = new HashMap<AGV, Crane>();
        pickingupToLoad = new HashMap<Crane, AGV>();
        containerToCrane = new HashMap<Container, Crane>();
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
        messageLog = Collections.synchronizedList(new ArrayList<String>());
        bargeCranes = new ArrayList<Crane>();
        for (int i = 1; i <= 8; i++) {
            Crane c = new Crane("CBA" + String.format("%03d", i), Crane.BargeCrane);
            c.node = pathFinder.getMapCBA().get(i - 1);
            bargeCranes.add(c);
            PrintMessage("Bargecrane Created - " + c.toString());
        }

        seaCranes = new ArrayList<Crane>();
        for (int i = 1; i <= 10; i++) {
            Crane c = new Crane("CSE" + String.format("%03d", i), Crane.SeaCrane);
            c.node = pathFinder.getMapCSE().get(i - 1);
            seaCranes.add(c);
            PrintMessage("SeaCrane Created - " + c.toString());
        }

        trainCranes = new ArrayList<Crane>();
        for (int i = 1; i <= 4; i++) {
            Crane c = new Crane("CTR" + String.format("%03d", i), Crane.TrainCrane);
            c.node = pathFinder.getMapCTR().get(i - 1);
            trainCranes.add(c);
            PrintMessage("Traincrane Created - " + c.toString());
        }

        lorreyCranes = new ArrayList<Crane>();
        for (int i = 1; i <= 20; i++) {
            Crane c = new Crane("CLO" + String.format("%03d", i), Crane.LorryCrane);
            c.node = pathFinder.getMapCLO().get(i - 1);
            c.startRange = 0;
            c.range = 1000;
            lorreyCranes.add(c);
            PrintMessage("Lorreystop Created - " + c.toString());
        }
        //Create buffers and agv's
        for (int i = 1; i <= 63; i++) {
            Buffer b = new Buffer();
            Crane c = new Crane("BFA" + String.format("%03d", i), Crane.BufferCrane);
            b.crane = c;
            PathNode upperNode = pathFinder.getMapBA().get(i - 1);
            PathNode downNode = pathFinder.getMapBB().get(i - 1);
            b.pathNodeUp = upperNode;
            b.pathNodeDown = downNode;
            for (int a = 0; a < 4; a++) {

                if (i <= 5) {
                    AGV agv = new AGV(upperNode, b);
                    b.ownedAGV.add(agv);
                    agvs.add(agv);
                    PrintMessage("AGV Created - " + agv.toString());
                } else {
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
        waitingForBufferCrane = new HashMap<AGV, Crane>();
        agvLoadedMovingHome = new ArrayList<AGV>();
        puttingToTransporter = new ArrayList<Crane>();
    }

    /**
     * Start simulator with tick of 1 second is 1 second
     */
    public void Start() {
        if (!playing) {
            playing = true;
        } else {
            Message m = new Message(Commands.PAUSE_PLAY, new Object[0]);
            this.sendMessage(m);
        }
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
        this.PrintMessage(Message.encodeMessage(Message));
        //    this.PrintMessage("Message send - " + Message.toString());
    }

    /**
     * tick in simulator to give commands
     */
    public void timerTick() {
        ArrayList<String> _temp = new ArrayList<String>(messageLog);
        messageLog.clear();
        for (String s : _temp) {
            window.WriteLogLine(s);
        }

        List<Transporter> arrivingTransporters = new ArrayList<Transporter>();
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
                    for (int i = 0; i < trainCranes.size(); i++) {
                        if (!dockedTransporter.containsKey(trainCranes.get(i))) {

                            Crane c = trainCranes.get(i);

                            t.setDockingPoint(c);

                            dockedTransporter.put(c, t);

                            break;
                        }
                    }
                    break;
                case TransportTypes.LORREY:
                    for (Crane crane : lorreyCranes) {
                        if (!dockedTransporter.containsKey(crane)) {
                            t.setDockingPoint(crane);
                            dockedTransporter.put(crane, t);
                            break;
                        }
                    }
                    break;

            }
            if (t.getDockingPoint() != null) {
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

        /*DEPARTURE STUFF STARTS HERE*/
        List<Transporter> departingTransporters = new ArrayList<Transporter>();
        for (int i = 0; i < allDepartingTransporters.size(); i++) {
            if (allDepartingTransporters.get(i).getDateArrival().before(simTime)) {
                departingTransporters.add(allDepartingTransporters.get(i));
                allDepartingTransporters.remove(i);
                i--;
            }
        }

        for (Transporter t : departingTransporters) {
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
                    for (int i = 0; i < trainCranes.size(); i++) {
                        if (!dockedTransporter.containsKey(trainCranes.get(i))) {
                            Crane c = trainCranes.get(i);
                            t.setDockingPoint(c);
                            dockedTransporter.put(c, t);
                            break;
                        }
                    }
                    break;
                case TransportTypes.LORREY:
                    for (Crane crane : lorreyCranes) {
                        if (!dockedTransporter.containsKey(crane)) {
                            t.setDockingPoint(crane);
                            dockedTransporter.put(crane, t);
                            break;
                        }
                    }
                    break;

            }
            if (t.getDockingPoint() != null) {
                currentTransporter.add(t);

                PrintMessage("Departing: " + t.toString());

                Message m = new Message(Commands.CREATE_TRANSPORTER, null);

                Object[] objects = new Object[3];
                objects[0] = t.id;
                objects[1] = t.getTransportType();
                objects[2] = t.getDockingPoint().id;
                m.setParameters(objects);
                server.sendCommand(m);
            }
        }

        /*
         ArrayList<Container> expectedContainers = new ArrayList();
         Container pivot = departingContainers.get(0);
         for (Container dc : departingContainers){
         if(pivot.getCargoCompanyDeparture().equalsIgnoreCase(dc.getCargoCompanyDeparture())
         && pivot.getDateDeparture().getTime() == dc.getDateDeparture().getTime()
         && pivot.getTransportTypeDeparture() == dc.getTransportTypeDeparture()){
         expectedContainers.add(dc);
         }
         }
         */
        /*DEPARTURE STUFF ENDS HERE*/
        for (Buffer buf : buffers) {
            ArrayList<Container> depContainers = new ArrayList<Container>();
            depContainers.addAll(buf.checkDepartingContainers(simTime));
            Collections.sort(depContainers, new ContainerDepartureComparer());

            if (depContainers.size() > 0 && buf.crane.getReady()) {
                Container toMove = depContainers.get(0);
                for (Transporter transporter : currentTransporter) {
                    int transportType = transporter.getTransportType();
                    if (transportType == toMove.getTransportTypeDeparture()) {
                        boolean ready = false;
                        CustomVector3f position = null;
                        Crane finalCrane = null;
                        switch (transportType) {
                            case TransportTypes.LORREY:
                                for (Crane crane : lorreyCranes) {
                                    position = transporter.getFreeLocation(crane.startRange, crane.range);
                                    if (crane.getReady() && position != null) {
                                        ready = true;
                                        finalCrane = crane;
                                        break;
                                    }
                                }
                                break;
                            case TransportTypes.BARGE:
                                for (Crane crane : bargeCranes) {
                                    position = transporter.getFreeLocation(crane.startRange, crane.range);
                                    if (crane.getReady() && position != null) {
                                        ready = true;
                                        finalCrane = crane;
                                        break;
                                    }
                                }
                                break;
                            case TransportTypes.SEASHIP:
                                for (Crane crane : seaCranes) {
                                    position = transporter.getFreeLocation(crane.startRange, crane.range);
                                    if (crane.getReady() && position != null) {
                                        ready = true;
                                        finalCrane = crane;
                                        break;
                                    }
                                }
                                break;
                            case TransportTypes.TRAIN:
                                for (Crane crane : trainCranes) {
                                    position = transporter.getFreeLocation(crane.startRange, crane.range);
                                    if (crane.getReady() && position != null) {
                                        ready = true;
                                        finalCrane = crane;
                                        break;
                                    }
                                }
                                break;

                        }
                        if (ready && position != null && finalCrane != null) {
                            toMove.setPosition(position);
                            transporter.reservePosition(toMove);
                            finalCrane.setIsReady(false);
                            containerToCrane.put(toMove, finalCrane);
                            
                            Message m = new Message(Commands.PICKUP_CONTAINER, null);
                            ArrayList<Object> params = new ArrayList<Object>();
                            params.add(buf.crane.id);

                            AGV toReserve = null;
                            if (toMove.getTransportTypeDeparture() == TransportTypes.TRAIN || toMove.getTransportTypeDeparture() == TransportTypes.SEASHIP) {
                                boolean up = true;

                                toReserve = buf.AGVAvailable(up);
                                if (toReserve == null) {
                                    up = false;
                                    toReserve = buf.AGVAvailable(up);
                                }
                                if (toReserve != null) {
                                    toReserve.setReady(false);
                                    //todo: link agv and container and add to list
                                    params.add(up);
                                }
                            } else {
                                boolean up = false;
                                toReserve = buf.AGVAvailable(up);
                                if (toReserve == null) {
                                    up = true;
                                    toReserve = buf.AGVAvailable(up);
                                }
                                if (toReserve != null) {
                                    toReserve.setReady(false);
                                    //todo: see above
                                    params.add(up);
                                }
                            }

                            params.add(toMove.getId());

                            m.setParameters(params.toArray());

                            if (toReserve != null) {
                                waitingForBufferCraneGive.put(buf.crane, toReserve);
                                this.sendMessage(m);
                            }
                            buf.crane.ready = false;
                            buf.removeContainer(toMove);
                            buf.crane.container = toMove;
                            break;
                        }
                    }
                }
            }
        }

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(simTime); // sets calendar time/date
        cal.add(Calendar.SECOND, Speed); // adds one minute
        simTime = cal.getTime(); // returns new date object, one hour in the future
        this.window.setTime(simTime);
    }

    /**
     * stop simulation
     */
    public void pause() {
        if (this.simTimer != null) {
            Message m = new Message(Commands.PAUSE_PLAY, new Object[0]);
            this.sendMessage(m);
            this.simTimer.cancel();
        }

    }

    /**
     * shutdowns client
     */
    public void shutDown() {//shutdowns client
        Message m = new Message(Commands.SHUTDOWN, null);
        m.setParameters(new Object[]{});
        this.sendMessage(m);
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
     * Let buffer crane pickup container from agv
     *
     * @param agv
     * @param bufferCrane
     */
    public void getContainerBuffer(AGV agv, Crane bufferCrane) {
        bufferCrane.ready = false;
        Message message = new Message(Commands.GET_CONTAINER, new Object[]{agv.name, bufferCrane.id});
        bufferCrane.container = agv.container;
        agv.container = null;
        waitingForBuferCranePickup.put(bufferCrane, agv);
        sendMessage(message);
    }

    /**
     * Print message on window
     *
     * @param message
     */
    public final void PrintMessage(final String message) {
        if (Speed < 5) {
            window.WriteLogLine(message);
        } else {
            messageLog.add(message);
        }
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
        } else if (agvLoadedMovingHome.contains(agv)) {
            Crane bufferCrane = agv.homeBuffer.crane;
            if (bufferCrane.ready) {
                getContainerBuffer(agv, bufferCrane);
            } else {
                waitingForBufferCrane.put(agv, bufferCrane);
            }
            agvLoadedMovingHome.remove(agv);
        } else if (movingToLoad.containsKey(agv)) {
            Crane crane = movingToLoad.get(agv);
            movingToLoad.remove(agv);
            pickingupToLoad.put(crane, agv);
            crane.container = agv.container;
            agv.container = null;
            Message m = new Message(Commands.GET_CONTAINER, new Object[]{agv.name, crane.id});
            this.sendMessage(m);

        }

    }

    private void createTransporters(List<Container> allContainers) {

        List<Container> allDepContainers = new ArrayList<Container>(allContainers);
        Container previousContainer = null;
        Transporter newTransporter = null;
        allArivingTransporters.clear();
        for (int i = 0; i < allContainers.size(); i++) {
            Container c = allContainers.get(i);
            if (previousContainer == null) {
                newTransporter = new Transporter(c.getTransportTypeArrival(), c.getDateArrival());
                newTransporter.reservePosition(c);
                newTransporter.loadContainer(c);
            } else {
                if (c.getDateArrival().getTime() == previousContainer.getDateArrival().getTime()
                        && c.getTransportTypeArrival() == newTransporter.getTransportType()
                        && c.getTransportTypeArrival() != TransportTypes.LORREY) {
                    newTransporter.reservePosition(c);
                    newTransporter.loadContainer(c);
                } else {
                    allArivingTransporters.add(newTransporter);
                    newTransporter = new Transporter(c.getTransportTypeArrival(), c.getDateArrival());
                    newTransporter.reservePosition(c);
                    newTransporter.loadContainer(c);
                }

            }
            previousContainer = c;
            allContainers.remove(i);
            i--;
        }
        allArivingTransporters.add(newTransporter);
        previousContainer = null;
        newTransporter = null;

        allDepartingTransporters.clear();
        for (int i = 0; i < allDepContainers.size(); i++) {
            Container c = allDepContainers.get(i);
            if (previousContainer == null) {
                newTransporter = new Transporter(c.getTransportTypeDeparture(), c.getDateDeparture());

                allDepartingTransporters.add(newTransporter);
            } else {
                if (!(c.getDateDeparture().getTime() == previousContainer.getDateDeparture().getTime()
                        || c.getTransportTypeDeparture() == newTransporter.getTransportType())
                        || c.getTransportTypeDeparture() == TransportTypes.LORREY) {
                    newTransporter = new Transporter(c.getTransportTypeDeparture(), c.getDateDeparture());

                    allDepartingTransporters.add(newTransporter);
                }
            }
            previousContainer = c;
            allDepContainers.remove(i);
            i--;
        }
        Collections.sort(allDepartingTransporters, new TransporterComparer());
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

    private List<AGV> getWaitingAGV(Crane bufferCrane) {
        ArrayList<AGV> _temp = new ArrayList();
        for (Map.Entry<AGV, Crane> e : waitingForBufferCrane.entrySet()) {
            AGV key = e.getKey();
            Crane value = e.getValue();
            if (bufferCrane.id.equalsIgnoreCase(value.id)) {
                _temp.add(key);
            }
        }

        return _temp;
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
        } else if (pickingupToLoad.containsKey(c)) {

            Message m = new Message(Commands.PUT_CONTAINER, new Object[]{c.id, c.container.getPosition().x, c.container.getPosition().y, c.container.getPosition().z, dockedTransporter.get(c).id});

            AGV a = pickingupToLoad.get(c);
            pickingupToLoad.remove(c);
            a.moveToHome(c, this);
            puttingToTransporter.add(c);
            this.sendMessage(m);
        } else if (puttingToTransporter.contains(c)) {

            Transporter t = dockedTransporter.get(c);
            t.loadContainer(c.container);
            
            puttingToTransporter.remove(c);
        } else if (waitingForCraneToPickUpFromAgv.containsKey(c)) {
            AGV v = waitingForCraneToPickUpFromAgv.get(c);
            if (v.container.getDateDeparture().before(simTime)) {
            } else {
                v.moveToHome(c, this);
                agvLoadedMovingHome.add(v);
            }
            waitingForCraneToPickUpFromAgv.remove(c);

        }//Kraan heeft container aan AGV gegeven
        else if (waitingForCraneToPutToAgv.containsKey(c)) {
            System.out.println("Move agv home");

            //AGV wordt naar huis gestuurd
            AGV v = waitingForCraneToPutToAgv.get(c);

            v.moveToHome(c, this);
            agvLoadedMovingHome.add(v);
            waitingForCraneToPutToAgv.remove(c);

            //kraan krijgt opdracht om nieuwe container te pakken
            Transporter t = dockedTransporter.get(c);
            if (t != null) {
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
                        System.out.println("no containers any more");
                    } else {
                        Message m = new Message(Commands.PICKUP_CONTAINER, null);
                        ArrayList<Object> params = new ArrayList<Object>();
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
                        Crane dock = getDockingPoint(t);
                        while (dock != null) {
                            dockedTransporter.remove(dock);
                            dock = getDockingPoint(t);
                        }
                    }
                    c.ready = true;
                }
            }
        } else {
            c.ready = true;
        }

    }

    /**
     * Crane from buffer is ready
     *
     * @param b
     */
    public void bufferCraneReady(Buffer b) {
        this.PrintMessage("Buffer ready - " + b.crane.id);
        b.crane.ready = true;
        if (b.crane.container == null && !waitingForCraneToPutToAgv.containsKey(b.crane)) {
            if (waitingForBufferCrane.containsValue(b.crane)) {
                List<AGV> _temp = getWaitingAGV(b.crane);
                if (_temp.size() > 1) {
                    AGV _closest = _temp.get(0);
                    for (AGV a : _temp) {
                        if (b.crane.lastX < 13) {
                            if (a.home.getId().toUpperCase().startsWith("BFA")) {
                                _closest = a;
                            }
                        } else {
                            if (a.home.getId().toUpperCase().startsWith("BFB")) {
                                _closest = a;
                            }
                        }
                    }
                    getContainerBuffer(_closest, b.crane);
                    waitingForBufferCrane.remove(_closest);

                } else if (_temp.size() > 0) {
                    getContainerBuffer(_temp.get(0), b.crane);
                    waitingForBufferCrane.remove(_temp.get(0));
                }
                /**/

            }
        } else if (waitingForBuferCranePickup.get(b.crane) != null) {
            waitingForBuferCranePickup.get(b.crane).setIsHome(true);
            waitingForBuferCranePickup.remove(b.crane);
            b.crane.lastX = (int) b.crane.container.getBufferPosition().x;
            Message message = new Message(Commands.PUT_CONTAINER, new Object[]{b.crane.id, b.crane.container.getBufferPosition().x,
                b.crane.container.getBufferPosition().y,
                b.crane.container.getBufferPosition().z});
            b.addContainer(b.crane.container);
            b.crane.container = null;
            b.crane.ready = false;
            this.sendMessage(message);
        } else if (waitingForBufferCraneGive.containsKey(b.crane)) {
            AGV agv = waitingForBufferCraneGive.get(b.crane);
            waitingForBufferCraneGive.remove(b.crane);
            if (agv != null) {
                putContainer(agv, b.crane);
            }
        } else {
            b.crane.container = null;
            AGV agv = waitingForCraneToPutToAgv.get(b.crane);
            Container con = agv.container;
            Crane cra = null;
            cra = containerToCrane.get(con);
            if (cra != null) {
                agv.moveToCrane(cra, this);
                agv.setIsHome(false);
                movingToLoad.put(agv, cra);
            }
        }
    }

    private void transporterReady(Transporter t) {
        if (t.getContainers() != null && t.getContainerCount() > 0) { //full transporter
            int maxCranes = 0;
            int minRange = 0;
            List<Crane> _cranes = new ArrayList<Crane>();
            switch (t.getTransportType()) {
                case TransportTypes.LORREY:
                    maxCranes = 1;
                    minRange = 2;
                    _cranes = new ArrayList<Crane>(lorreyCranes);
                    break;
                case TransportTypes.SEASHIP:
                    maxCranes = 10;
                    minRange = 2;
                    _cranes = new ArrayList<Crane>(seaCranes);
                    break;
                case TransportTypes.BARGE:
                    maxCranes = 2;
                    minRange = 3;
                    _cranes = new ArrayList<Crane>(bargeCranes);
                    break;
                case TransportTypes.TRAIN:
                    maxCranes = 1;
                    minRange = 30;
                    _cranes = new ArrayList<Crane>(trainCranes);
                    break;
            }
            ArrayList<Crane> usingCranes = new ArrayList();
            for (int i = _cranes.indexOf(t.getDockingPoint()); i < _cranes.size()
                    && i < _cranes.indexOf(t.getDockingPoint()) + maxCranes; i++) {
                usingCranes.add(_cranes.get(i));

            }

            int range = (int) Math.ceil(t.getLenghtTransporter() / usingCranes.size());
            if (range < minRange) {
                range = minRange;
                int numberofcranes = (int) Math.ceil(t.getLenghtTransporter() / range);
                while (numberofcranes > usingCranes.size()) {
                    usingCranes.remove(usingCranes.remove(usingCranes.size() - 1));
                }
            }

            for (int i = 0; i < usingCranes.size(); i++) {
                Crane crane = usingCranes.get(i);
                crane.startRange = i * range;
                if (i == usingCranes.size() - 1) {
                    crane.range = 40;
                } else {
                    crane.range = range;
                }
                dockedTransporter.put(crane, t);
            }

            for (Crane crane : usingCranes) {
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

                Message m = new Message(Commands.PICKUP_CONTAINER, null);
                ArrayList<Object> params = new ArrayList<Object>();
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
        List<Buffer> preverdBuffers = new ArrayList<Buffer>();
        PreferedAGV prefrence = PreferedAGV.UP;
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
            if (dockingpoint.id.contains("1") || dockingpoint.id.contains("2")) {
                startBuffer = 1;
                endBuffer = 25;
            } else {
                startBuffer = 53;
                endBuffer = 63;
            }
            prefrence = PreferedAGV.UP;
        } else {
            if (dockingpoint.id.contains("1") || dockingpoint.id.contains("2")
                    || dockingpoint.id.contains("3")
                    || dockingpoint.id.contains("4")) {
                startBuffer = 1;
                endBuffer = 5;
                prefrence = PreferedAGV.UP;
            } else {
                startBuffer = 5;
                endBuffer = 20;
                prefrence = PreferedAGV.DOWN;
            }

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
            if (agv != null) {
                CustomVector3f bestpos = null;
                if (agv.home.getId().toUpperCase().startsWith("BFA")) {
                    bestpos = b.findBestBufferPlace(toMove, true);
                } else {
                    bestpos = b.findBestBufferPlace(toMove, false);
                }
                if (bestpos != null) {
                    toMove.setBufferPosition(bestpos);
                    b.reservePosition(toMove);
                    agv.moveToCrane(dockingpoint, this);
                    waitingToBeReadyAtCrane.put(agv, dockingpoint);
                    agv.setIsHome(false);
                    agv.setReady(false);
                    return true;
                }
            }
        }
        return false;
    }

    public void setContainers(List<Container> containers) {
        this.containers = new ArrayList(containers);
        Collections.sort(containers, new ContainerComparer());
        this.PrintMessage("Total containers - " + containers.size());
        createTransporters(containers);
        this.PrintMessage("Total arriving transporters  - " + allArivingTransporters.size());
        this.PrintMessage("Total departing transporters  - " + allDepartingTransporters.size());
        if (allArivingTransporters.size() > 0) {
            simTime = allArivingTransporters.get(0).getContainers().get(0).getDateArrival();
        }
    }

    public void recievedMessage(String message) {
        //   PrintMessage(message);
        Message m = Message.decodeMessage(message);

        if (!((String) m.getParameters()[0]).equalsIgnoreCase("simulator")) {

            System.out.println(m.getCommand());
            int id = 0;
            if (((String) m.getParameters()[0]).length() >= 6) {
                id = Integer.parseInt(((String) m.getParameters()[0]).substring(3, 6));
            }
            if (m.getCommand() == Commands.RETREIVE_INFO) {
                sendContainerInfo((String) m.getParameters()[0]);
            } else if (((String) m.getParameters()[0]).toLowerCase().startsWith("BFA".toLowerCase())) {
                bufferCraneReady(buffers.get(id - 1));
            } else {
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
    }

    private void sendContainerInfo(String id) {
        Message m = new Message(Commands.RETREIVE_INFO, null);
        ArrayList<Object> params = new ArrayList<Object>();
        int idInt = 0;
        if (id.length() >= 6) {
            idInt = Integer.parseInt(id.substring(3, 6));
        }
        System.out.println(id);

        if (id.toLowerCase().startsWith("bfa")) {
            Crane cr = buffers.get(idInt - 1).crane;
            params.add(cr.id);
            params.add(cr.container == null ? "-" : cr.container.getId());
            params.add(cr.ready);
        } else {

            switch (id.toLowerCase().charAt(0)) {
                case 't':
                    for (Transporter t : currentTransporter) {
                        if (t.id.equalsIgnoreCase(id)) {
                            params.add(t.id);
                            params.add(t.getContainerCount());
                            params.add(t.getDateArrival());
                            params.add(t.getDockingPoint().id);
                            params.add(t.getLenghtTransporter());
                            params.add(t.getTransportType());
                            break;
                        }
                    }
                    break;
                case 'a':
                    AGV agv = agvs.get(Integer.parseInt(id.substring(3, 6)) - 1);
                    params.add(agv.name);
                    params.add(agv.homeBuffer.id);
                    params.add(agv.isReady());
                    break;

                case 'c':

                    Crane c = null;
                    if (id.substring(1, 3).equalsIgnoreCase("SE")) {
                        c = seaCranes.get(idInt - 1);
                    } else if (id.substring(1, 3).equalsIgnoreCase("BA")) {
                        c = (bargeCranes.get(idInt - 1));
                    } else if (id.substring(1, 3).equalsIgnoreCase("LO")) {
                        c = (lorreyCranes.get(idInt - 1));
                    } else if (id.substring(1, 3).equalsIgnoreCase("TR")) {
                        c = (trainCranes.get(idInt - 1));
                    }
                    params.add(c.id != null ? c.id : "JOHN DOE");
                    params.add(c.container == null ? "-" : c.container.getId());
                    params.add(c.ready);
                    break;
                case 'i':
                    for (Container con : this.containers) {
                        if (con.getId().equals(id)) {
                            params.add(con.getId());
                            params.add(con.getOwner());
                            params.add(con.getDateArrival());
                            params.add(con.getTransportTypeArrival());
                            params.add(con.getCargoCompanyArrival());
                            params.add(con.getDateDeparture());
                            params.add(con.getTransportTypeDeparture());
                            params.add(con.getCargoCompanyDeparture());
                            params.add(con.getContents());
                            params.add(con.getContentType());
                            params.add(con.getContentDanger());
                            System.out.println("yes");
                            break;
                        }
                    }
                    break;
            }
        }
        m.setParameters(params.toArray());
        this.sendMessage(m);
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

        List<Crane> cranes = new ArrayList<Crane>();
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

    public void setSpeed(int speedNumber) {

        this.PrintMessage("Change speed - " + speedNumber + "x");
        this.Speed = speedNumber;
        Message m = new Message(Commands.CHANGE_SPEEED, new Object[]{speedNumber});
        this.sendMessage(m);
    }
}
